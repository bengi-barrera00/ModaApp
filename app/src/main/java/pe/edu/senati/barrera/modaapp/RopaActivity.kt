package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.edu.senati.barrera.modaapp.adapter.RopaAdapter
import pe.edu.senati.barrera.modaapp.util.Ui
import pe.edu.senati.barrera.modaapp.data.RopaDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityRopaBinding

class RopaActivity : AppCompatActivity() {

    private lateinit var b: ActivityRopaBinding
    private val dao by lazy { RopaDao(this) }
    private val adapter = RopaAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityRopaBinding.inflate(layoutInflater)
        setContentView(b.root)

        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }

        b.rvRopa.adapter = adapter
        b.fabNuevo.setOnClickListener {
            startActivity(Intent(this, RopaFormActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.actualizar(dao.listar())
    }
}