package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import pe.edu.senati.barrera.modaapp.data.ClienteDao
import pe.edu.senati.barrera.modaapp.data.PedidoDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityPedidoBinding
import pe.edu.senati.barrera.modaapp.model.Carrito
import pe.edu.senati.barrera.modaapp.model.Cliente
import pe.edu.senati.barrera.modaapp.util.Ui

class PedidoActivity : AppCompatActivity() {

    private enum class Etapa { TELEFONO, EXISTENTE, NUEVO }

    private lateinit var b: ActivityPedidoBinding
    private val clienteDao by lazy { ClienteDao(this) }
    private val pedidoDao by lazy { PedidoDao(this) }
    private var etapa = Etapa.TELEFONO
    private var cliente: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Carrito.items.isEmpty()) {
            finish()
            return
        }

        enableEdgeToEdge()
        b = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(b.root)

        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }

        val n = Carrito.items.size
        b.tvResumen.text = resources.getQuantityString(R.plurals.prendas, n, n)
        b.tvMonto.text = "S/ %.2f".format(Carrito.total())

        b.etTelefono.doAfterTextChanged { reiniciar() }

        b.btnAccion.setOnClickListener {
            when (etapa) {
                Etapa.TELEFONO -> continuar()
                Etapa.EXISTENTE -> confirmar(cliente!!.id)
                Etapa.NUEVO -> registrarYConfirmar()
            }
        }
    }

    private fun reiniciar() {
        etapa = Etapa.TELEFONO
        cliente = null
        b.tilTelefono.error = null
        b.tvEstado.visibility = View.GONE
        b.llNuevo.visibility = View.GONE
        b.btnAccion.setText(R.string.continuar)
    }

    private fun continuar() {
        val telefono = b.etTelefono.text.toString().trim()
        if (!Regex("\\d{9}").matches(telefono)) {
            b.tilTelefono.error = getString(R.string.err_telefono)
            return
        }
        b.tilTelefono.error = null

        val encontrado = clienteDao.buscarPorTelefono(telefono)
        if (encontrado != null) {
            cliente = encontrado
            etapa = Etapa.EXISTENTE
            b.tvEstado.text = getString(R.string.hola, encontrado.nombres)
            b.tvEstado.setTextColor(getColor(R.color.rosa_oscuro))
            b.llNuevo.visibility = View.GONE
        } else {
            cliente = null
            etapa = Etapa.NUEVO
            b.tvEstado.text = getString(R.string.numero_nuevo)
            b.tvEstado.setTextColor(getColor(R.color.rojo))
            b.llNuevo.visibility = View.VISIBLE
        }
        b.tvEstado.visibility = View.VISIBLE
        b.btnAccion.setText(R.string.confirmar_pedido)
    }

    private fun registrarYConfirmar() {
        val nombres = b.etNombres.text.toString().trim()
        val apellidos = b.etApellidos.text.toString().trim()

        b.tilNombres.error = if (nombres.isEmpty()) getString(R.string.err_campo) else null
        b.tilApellidos.error = if (apellidos.isEmpty()) getString(R.string.err_campo) else null
        if (nombres.isEmpty() || apellidos.isEmpty()) return

        val id = clienteDao.insertar(b.etTelefono.text.toString().trim(), nombres, apellidos)
        if (id == -1L) {
            Toast.makeText(this, "No se pudo registrar el cliente", Toast.LENGTH_SHORT).show()
            return
        }
        confirmar(id.toInt())
    }

    private fun confirmar(idCliente: Int) {
        try {
            val idPedido = pedidoDao.registrar(idCliente, Carrito.items.toList())
            Carrito.vaciar()
            startActivity(
                Intent(this, PedidoExitoActivity::class.java).putExtra("idPedido", idPedido)
            )
            finish()
        } catch (e: SQLException) {
            Toast.makeText(this, "No se pudo registrar el pedido", Toast.LENGTH_LONG).show()
        }
    }
}