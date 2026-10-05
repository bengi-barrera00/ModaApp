package pe.edu.senati.barrera.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pe.edu.senati.barrera.modaapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var b: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(b.root)

        ViewCompat.setOnApplyWindowInsetsListener(b.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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