package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.data.DBHelper
import pe.edu.senati.barrera.modaapp.data.PedidoDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityPedidoExitoBinding
import pe.edu.senati.barrera.modaapp.model.PedidoResumen
import pe.edu.senati.barrera.modaapp.util.Ui
import pe.edu.senati.barrera.modaapp.util.Whatsapp

class PedidoExitoActivity : AppCompatActivity() {

    private lateinit var b: ActivityPedidoExitoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val p = PedidoDao(this).obtenerResumen(intent.getLongExtra("idPedido", -1L))
        if (p == null) {
            finish()
            return
        }

        enableEdgeToEdge()
        b = ActivityPedidoExitoBinding.inflate(layoutInflater)
        setContentView(b.root)
        Ui.ajustar(this, b.root, b.header)

        b.tvNumero.text = getString(R.string.pedido_registrado, p.id)
        b.tvResumen.text = lineasTexto(p)
        b.tvTotal.text = "Total: S/ %.2f".format(p.total)

        b.btnMiWhatsapp.setOnClickListener {
            Whatsapp.abrir(this, p.telefono, mensajeCliente(p))
        }
        b.btnTienda.setOnClickListener {
            Whatsapp.abrir(this, DBHelper(this).telefonoAdmin(), mensajeTienda(p))
        }
        b.btnCatalogo.setOnClickListener { irAlCatalogo() }
        onBackPressedDispatcher.addCallback(this) { irAlCatalogo() }
    }

    private fun irAlCatalogo() {
        startActivity(
            Intent(this, CatalogoActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        )
        finish()
    }

    private fun lineasTexto(p: PedidoResumen) = p.lineas.joinToString("\n") {
        "- ${it.modelo} (talla ${it.talla ?: "-"}, ${it.color ?: "-"}) x${it.cantidad}"
    }

    private fun mensajeCliente(p: PedidoResumen) = buildString {
        appendLine("Boutique Moda Urbana - Pedido #${p.id}")
        appendLine(lineasTexto(p))
        appendLine("Total: S/ %.2f".format(p.total))
        append("Estado: ${p.estado}")
    }

    private fun mensajeTienda(p: PedidoResumen) = buildString {
        appendLine("NUEVO PEDIDO #${p.id}")
        appendLine("Cliente: ${p.nombres} ${p.apellidos}")
        appendLine("Telefono: ${p.telefono}")
        appendLine(lineasTexto(p))
        appendLine("Total: S/ %.2f".format(p.total))
        append("Estado: ${p.estado}")
    }
}