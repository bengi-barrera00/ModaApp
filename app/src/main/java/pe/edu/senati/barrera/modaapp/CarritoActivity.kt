package pe.edu.senati.barrera.modaapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.adapter.CarritoAdapter
import pe.edu.senati.barrera.modaapp.databinding.ActivityCarritoBinding
import pe.edu.senati.barrera.modaapp.model.Carrito
import pe.edu.senati.barrera.modaapp.model.ItemCarrito
import pe.edu.senati.barrera.modaapp.util.Ui

class CarritoActivity : AppCompatActivity() {

    private lateinit var b: ActivityCarritoBinding
    private val adapter = CarritoAdapter(emptyList()) { item -> confirmarQuitar(item) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(b.root)

        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }
        b.rvCarrito.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        refrescar()
    }

    private fun refrescar() {
        adapter.actualizar(Carrito.items.toList())
        val vacio = Carrito.items.isEmpty()
        b.tvVacio.visibility = if (vacio) View.VISIBLE else View.GONE
        b.tvTotal.text = "S/ %.2f".format(Carrito.total())
        b.btnPedido.isEnabled = !vacio
    }

    private fun confirmarQuitar(item: ItemCarrito) {
        AlertDialog.Builder(this)
            .setTitle(item.ropa.modelo)
            .setMessage("¿Quitar esta prenda del carrito?")
            .setPositiveButton("Quitar") { _, _ ->
                Carrito.quitar(item)
                refrescar()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}