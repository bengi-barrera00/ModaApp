package pe.edu.senati.barrera.modaapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.adapter.FilaAdapter
import pe.edu.senati.barrera.modaapp.data.ReporteDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityClientesBinding
import pe.edu.senati.barrera.modaapp.util.Ui

class ClientesActivity : AppCompatActivity() {

    private lateinit var b: ActivityClientesBinding
    private val adapter = FilaAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(b.root)
        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }
        b.rvClientes.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val lista = ReporteDao(this).clientesConPedidos()
        adapter.actualizar(lista)
        b.tvVacio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
    }
}