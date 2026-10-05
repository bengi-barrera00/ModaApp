package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.edu.senati.barrera.modaapp.util.Ui
import pe.edu.senati.barrera.modaapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var b: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(b.root)

        Ui.ajustar(this, b.root, b.header)

        val usuario = intent.getStringExtra("usuario") ?: ""
        b.tvUsuario.text = getString(R.string.hola, usuario)
        b.tvRol.text = getString(R.string.rol, intent.getStringExtra("rol") ?: "")
        b.tvAvatar.text = usuario.take(1).uppercase()

        b.tvUsuario.text = "${intent.getStringExtra("usuario")} · ${intent.getStringExtra("rol")}"
        b.btnRopa.setOnClickListener { startActivity(Intent(this, RopaActivity::class.java)) }
        b.btnPedidos.setOnClickListener { startActivity(Intent(this, PedidosActivity::class.java)) }
        b.btnClientes.setOnClickListener { startActivity(Intent(this, ClientesActivity::class.java)) }
        b.btnReportes.setOnClickListener { startActivity(Intent(this, ReportesActivity::class.java)) }
        b.btnSalir.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}