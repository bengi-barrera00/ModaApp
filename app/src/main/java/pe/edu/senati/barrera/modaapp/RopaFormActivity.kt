package pe.edu.senati.barrera.modaapp

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pe.edu.senati.barrera.modaapp.data.RopaDao
import pe.edu.senati.barrera.modaapp.databinding.ActivityRopaFormBinding
import pe.edu.senati.barrera.modaapp.model.Categoria
import pe.edu.senati.barrera.modaapp.model.Ropa
import pe.edu.senati.barrera.modaapp.util.Fotos
import pe.edu.senati.barrera.modaapp.util.Ui
import java.io.File

class RopaFormActivity : AppCompatActivity() {

    private lateinit var b: ActivityRopaFormBinding
    private val dao by lazy { RopaDao(this) }
    private var rutaFoto: String? = null
    private var rutaOriginal: String? = null
    private var idEdicion = 0
    private val tallas = listOf("XS", "S", "M", "L", "XL")
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
            tallas
        )

        b.btnFoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        b.btnGuardar.setOnClickListener { guardar() }
        b.btnEliminar.setOnClickListener { confirmarEliminar() }

        idEdicion = intent.getIntExtra("id", 0)
        if (idEdicion != 0) cargarParaEditar()
    }

    private fun cargarParaEditar() {
        val r = dao.obtener(idEdicion)
        if (r == null) {
            finish()
            return
        }

        b.tvTitulo.setText(R.string.editar_prenda)
        b.etModelo.setText(r.modelo)
        b.etMarca.setText(r.marca)
        b.etColor.setText(r.color)
        b.etPrecio.setText(r.precio.toString())
        b.etCantidad.setText(r.cantidad.toString())
        b.spCategoria.setSelection(categorias.indexOfFirst { it.id == r.idCategoria }.coerceAtLeast(0))
        b.spTalla.setSelection(tallas.indexOf(r.talla).coerceAtLeast(0))

        rutaFoto = r.foto
        rutaOriginal = r.foto
        b.ivFoto.setImageBitmap(Fotos.cargar(rutaFoto, 600))

        b.btnGuardar.setText(R.string.actualizar)
        b.btnEliminar.visibility = View.VISIBLE
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

        val ropa = Ropa(
            id = idEdicion,
            modelo = modelo,
            idCategoria = categorias[b.spCategoria.selectedItemPosition].id,
            talla = b.spTalla.selectedItem.toString(),
            marca = b.etMarca.text.toString().trim(),
            color = b.etColor.text.toString().trim(),
            precio = precio,
            cantidad = cantidad,
            foto = rutaFoto!!
        )

        if (idEdicion == 0) {
            dao.insertar(ropa)
            Toast.makeText(this, "Prenda guardada", Toast.LENGTH_SHORT).show()
        } else {
            dao.actualizar(ropa)
            if (rutaOriginal != null && rutaOriginal != rutaFoto) File(rutaOriginal!!).delete()
            Toast.makeText(this, "Prenda actualizada", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun confirmarEliminar() {
        AlertDialog.Builder(this)
            .setTitle(R.string.eliminar)
            .setMessage("¿Eliminar esta prenda?")
            .setPositiveButton(R.string.eliminar) { _, _ -> eliminar() }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun eliminar() {
        try {
            dao.eliminar(idEdicion)
            rutaFoto?.let { File(it).delete() }
            Toast.makeText(this, "Prenda eliminada", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: SQLiteConstraintException) {
            Toast.makeText(this, R.string.err_eliminar, Toast.LENGTH_LONG).show()
        }
    }
}