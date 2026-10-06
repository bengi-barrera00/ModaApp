package pe.edu.senati.barrera.modaapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.adapter.LineaAdapter
import pe.edu.senati.barrera.modaapp.data.PedidoDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityDetallePedidoBinding
import pe.edu.senati.barrera.modaapp.util.Ui

class DetallePedidoActivity : AppCompatActivity() {

    private lateinit var b: ActivityDetallePedidoBinding
    private val dao by lazy { PedidoDao(this) }
    private val adapter = LineaAdapter(emptyList())
    private var idPedido = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityDetallePedidoBinding.inflate(layoutInflater)
        setContentView(b.root)
        Ui.ajustar(this, b.root, b.header)

        idPedido = intent.getLongExtra("idPedido", -1L)
        b.btnAtras.setOnClickListener { finish() }
        b.rvLineas.adapter = adapter
        b.btnAtender.setOnClickListener { confirmar() }
        mostrar()
    }

    private fun mostrar() {
        val p = dao.obtenerResumen(idPedido)
        if (p == null) {
            finish()
            return
        }
        b.tvCliente.text = "${p.nombres} ${p.apellidos}"
        b.tvTelefono.text = "Tel: ${p.telefono}"
        b.tvEstado.text = "Pedido #${p.id} · ${p.estado}"
        b.tvTotal.text = "Total: S/ %.2f".format(p.total)
        adapter.actualizar(p.lineas)
        b.btnAtender.visibility = if (p.estado == "PENDIENTE") View.VISIBLE else View.GONE
    }

    private fun confirmar() {
        AlertDialog.Builder(this)
            .setTitle(R.string.marcar_atendido)
            .setMessage(R.string.confirmar_atender)
            .setPositiveButton(R.string.si) { _, _ ->
                val sinStock = dao.atender(idPedido)
                if (sinStock == null) {
                    Toast.makeText(this, R.string.pedido_atendido, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(
                        this, getString(R.string.stock_insuficiente, sinStock), Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
}