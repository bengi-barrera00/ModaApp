package pe.edu.senati.barrera.modaapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.adapter.FilaAdapter
import pe.edu.senati.barrera.modaapp.data.ReporteDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityReportesBinding
import pe.edu.senati.barrera.modaapp.util.Ui

class ReportesActivity : AppCompatActivity() {

    private lateinit var b: ActivityReportesBinding
    private val dao by lazy { ReporteDao(this) }
    private val adapter = FilaAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(b.root)
        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }
        b.rvStock.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val mes = dao.atendidosDelMes()
        b.tvAtendidos.text = mes.pedidos.toString()
        b.tvMonto.text = "S/ %.2f".format(mes.monto)
        adapter.actualizar(dao.stockPorPrenda())
    }
}