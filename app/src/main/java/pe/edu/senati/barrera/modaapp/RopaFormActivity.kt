package pe.edu.senati.barrera.modaapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.data.RopaDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityRopaFormBinding
import pe.edu.senati.barrera.modaapp.model.Categoria
import pe.edu.senati.barrera.modaapp.model.Ropa
import pe.edu.senati.barrera.modaapp.util.Fotos
import androidx.activity.enableEdgeToEdge
import pe.edu.senati.barrera.modaapp.util.Ui
import java.io.File

class RopaFormActivity : AppCompatActivity() {

    private lateinit var b: ActivityRopaFormBinding
    private val dao by lazy { RopaDao(this) }
    private var rutaFoto: String? = null
    private lateinit var categorias: List<Categoria>

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val archivo = File(filesDir, "ropa_${System.currentTimeMillis()}.jpg")
            contentResolver.openInputStream(uri)?.use { input ->
                archivo.outputStream().use { input.copyTo(it) }
            }
            rutaFoto = archivo.absolutePath
            b.ivFoto.setImageBitmap(Fotos.cargar(rutaFoto!!, 600))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityRopaFormBinding.inflate(layoutInflater)
        setContentView(b.root)

        Ui.ajustar(this, b.root, b.header)
        b.btnAtras.setOnClickListener { finish() }

        categorias = dao.listarCategorias()
        b.spCategoria.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categorias.map { it.nombre }
        )

        b.spTalla.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("XS", "S", "M", "L", "XL")
        )

        b.btnFoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        b.btnGuardar.setOnClickListener { guardar() }
    }

    private fun guardar() {
        if (categorias.isEmpty()) {
            Toast.makeText(this, "No hay categorías registradas", Toast.LENGTH_SHORT).show()
            return
        }

        val modelo = b.etModelo.text.toString().trim()
        val precio = b.etPrecio.text.toString().toDoubleOrNull()
        val cantidad = b.etCantidad.text.toString().toIntOrNull()

        b.tilModelo.error = if (modelo.isEmpty()) getString(R.string.err_campo) else null
        b.tilPrecio.error = if (precio == null || precio <= 0) "Precio mayor a 0" else null
        b.tilCantidad.error = if (cantidad == null || cantidad < 0) getString(R.string.err_campo) else null

        if (rutaFoto == null) Toast.makeText(this, "Elige una foto", Toast.LENGTH_SHORT).show()

        if (modelo.isEmpty() || precio == null || precio <= 0 || cantidad == null || cantidad < 0 || rutaFoto == null) return

        dao.insertar(
            Ropa(
                modelo = modelo,
                idCategoria = categorias[b.spCategoria.selectedItemPosition].id,
                talla = b.spTalla.selectedItem.toString(),
                marca = b.etMarca.text.toString().trim(),
                color = b.etColor.text.toString().trim(),
                precio = precio,
                cantidad = cantidad,
                foto = rutaFoto!!
            )
        )

        Toast.makeText(this, "Prenda guardada", Toast.LENGTH_SHORT).show()
        finish()
    }
}