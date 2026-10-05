package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import pe.edu.senati.barrera.modaapp.adapter.CatalogoAdapter
import pe.edu.senati.barrera.modaapp.data.RopaDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityCatalogoBinding
import pe.edu.senati.barrera.modaapp.util.Ui

class CatalogoActivity : AppCompatActivity() {

    private lateinit var b: ActivityCatalogoBinding
    private val dao by lazy { RopaDao(this) }
    private val adapter = CatalogoAdapter(emptyList())
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
    }

    private fun mostrar() = adapter.actualizar(dao.listarDisponibles(categoriaActual))

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