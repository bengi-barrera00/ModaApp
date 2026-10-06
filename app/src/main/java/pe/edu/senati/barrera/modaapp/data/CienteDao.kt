package pe.edu.senati.barrera.modaapp.data

import android.content.ContentValues
import android.content.Context
import pe.edu.senati.barrera.modaapp.model.Cliente
import pe.edu.senati.barrera.modaapp.util.Fechas

class ClienteDao(ctx: Context) {
    private val helper = DBHelper(ctx)

    fun buscarPorTelefono(telefono: String): Cliente? {
        helper.readableDatabase.rawQuery(
            "SELECT id, telefono, nombres, apellidos FROM cliente WHERE telefono=?",
            arrayOf(telefono)
        ).use { c ->
            return if (c.moveToFirst())
                Cliente(c.getInt(0), c.getString(1), c.getString(2), c.getString(3))
            else null
        }
    }

    fun insertar(telefono: String, nombres: String, apellidos: String): Long {
        val v = ContentValues().apply {
            put("telefono", telefono)
            put("nombres", nombres)
            put("apellidos", apellidos)
            put("fecha_registro", Fechas.ahora())
        }
        return helper.writableDatabase.insert("cliente", null, v)
    }
}