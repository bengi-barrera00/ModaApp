package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.adapter.FilaAdapter
import pe.edu.senati.barrera.modaapp.data.PedidoDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityPedidosBinding
import pe.edu.senati.barrera.modaapp.util.Ui

class PedidosActivity : AppCompatActivity() {

    private lateinit var b: ActivityPedidosBinding
    private val dao by lazy { PedidoDao(this) }
    private lateinit var adapter: FilaAdapter
    private var estado = "PENDIENTE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(b.root)
        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }

        adapter = FilaAdapter(emptyList()) { fila ->
            startActivity(
                Intent(this, DetallePedidoActivity::class.java).putExtra("idPedido", fila.id)
            )
        }
        b.rvPedidos.adapter = adapter

        b.toggle.addOnButtonCheckedListener { _, id, checked ->
            if (checked) {
                estado = if (id == R.id.btnPendientes) "PENDIENTE" else "ATENDIDO"
                cargar()
            }
        }
        b.toggle.check(R.id.btnPendientes)
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun cargar() {
        val lista = dao.listarPorEstado(estado)
        adapter.actualizar(lista)
        b.tvVacio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
    }
}