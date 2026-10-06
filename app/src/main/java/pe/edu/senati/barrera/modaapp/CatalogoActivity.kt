package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import pe.edu.senati.barrera.modaapp.adapter.CatalogoAdapter
import pe.edu.senati.barrera.modaapp.data.RopaDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityCatalogoBinding
import pe.edu.senati.barrera.modaapp.model.Carrito
import pe.edu.senati.barrera.modaapp.model.Ropa
import pe.edu.senati.barrera.modaapp.util.Ui

class CatalogoActivity : AppCompatActivity() {

    private lateinit var b: ActivityCatalogoBinding
    private val dao by lazy { RopaDao(this) }
    private val adapter = CatalogoAdapter(emptyList()) { r -> pedirCantidad(r) }
    private var categoriaActual: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(b.root)

        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }
        b.btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        b.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        b.rvCatalogo.adapter = adapter

        agregarChip("Todas", null, marcado = true)
        dao.listarCategorias().forEach { agregarChip(it.nombre, it.id) }

        b.chipsCategoria.setOnCheckedStateChangeListener { group, ids ->
            val chip = ids.firstOrNull()?.let { group.findViewById<Chip>(it) }
            categoriaActual = chip?.tag as Int?
            mostrar()
        }
    }

    override fun onResume() {
        super.onResume()
        mostrar()
        actualizarContador()
    }

    private fun mostrar() = adapter.actualizar(dao.listarDisponibles(categoriaActual))

    private fun actualizarContador() {
        val n = Carrito.unidades()
        b.tvContador.text = n.toString()
        b.tvContador.visibility = if (n > 0) View.VISIBLE else View.GONE
    }

    private fun pedirCantidad(r: Ropa) {
        val disponible = r.cantidad - Carrito.cantidadDe(r.id)
        if (disponible <= 0) {
            Toast.makeText(this, "Ya agregaste todo el stock disponible", Toast.LENGTH_SHORT).show()
            return
        }

        val picker = NumberPicker(this).apply {
            minValue = 1
            maxValue = disponible
            value = 1
            wrapSelectorWheel = false
        }
        val caja = FrameLayout(this).apply {
            setPadding(0, 24, 0, 0)
            addView(
                picker,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }

        AlertDialog.Builder(this)
            .setTitle(r.modelo)
            .setMessage("Disponible: $disponible")
            .setView(caja)
            .setPositiveButton("Agregar") { _, _ ->
                Carrito.agregar(r, picker.value)
                actualizarContador()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun agregarChip(texto: String, idCat: Int?, marcado: Boolean = false) {
        val chip = Chip(this).apply {
            text = texto
            tag = idCat
            id = View.generateViewId()
            isCheckable = true
            isChecked = marcado
        }
        b.chipsCategoria.addView(chip)
    }
}