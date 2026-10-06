package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import pe.edu.senati.barrera.modaapp.adapter.RopaAdapter
import pe.edu.senati.barrera.modaapp.data.RopaDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityRopaBinding
import pe.edu.senati.barrera.modaapp.util.Ui

class RopaActivity : AppCompatActivity() {

    private lateinit var b: ActivityRopaBinding
    private val dao by lazy { RopaDao(this) }
    private val adapter = RopaAdapter(emptyList()) { r ->
        startActivity(Intent(this, RopaFormActivity::class.java).putExtra("id", r.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityRopaBinding.inflate(layoutInflater)
        setContentView(b.root)

        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }

        b.rvRopa.adapter = adapter
        b.etBuscar.doAfterTextChanged { cargar() }
        b.fabNuevo.setOnClickListener {
            startActivity(Intent(this, RopaFormActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun cargar() = adapter.actualizar(dao.listar(b.etBuscar.text.toString()))
}