package pe.edu.senati.barrera.modaapp.data

import android.content.ContentValues
import android.content.Context
import pe.edu.senati.barrera.modaapp.model.Categoria
import pe.edu.senati.barrera.modaapp.model.Ropa

class RopaDao(ctx: Context) {
    private val helper = DBHelper(ctx)

    fun listarCategorias(): List<Categoria> {
        val lista = mutableListOf<Categoria>()
        helper.readableDatabase.rawQuery("SELECT id, nombre FROM categoria ORDER BY id", null).use {
            while (it.moveToNext()) lista.add(Categoria(it.getInt(0), it.getString(1)))
        }
        return lista
    }

    fun insertar(r: Ropa): Long {
        val v = ContentValues().apply {
            put("modelo", r.modelo); put("id_categoria", r.idCategoria); put("talla", r.talla)
            put("marca", r.marca); put("color", r.color); put("precio", r.precio)
            put("cantidad", r.cantidad); put("foto", r.foto)
        }
        return helper.writableDatabase.insert("ropa", null, v)
    }

    fun listar(): List<Ropa> = consultar("SELECT * FROM ropa ORDER BY id DESC", null)

    fun listarDisponibles(idCategoria: Int?): List<Ropa> =
        if (idCategoria == null)
            consultar("SELECT * FROM ropa WHERE cantidad > 0 ORDER BY id DESC", null)
        else
            consultar("SELECT * FROM ropa WHERE cantidad > 0 AND id_categoria=? ORDER BY id DESC",
                arrayOf(idCategoria.toString()))

    private fun consultar(sql: String, args: Array<String>?): List<Ropa> {
        val lista = mutableListOf<Ropa>()
        helper.readableDatabase.rawQuery(sql, args).use { c ->
            while (c.moveToNext()) lista.add(
                Ropa(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("modelo")),
                    c.getInt(c.getColumnIndexOrThrow("id_categoria")),
                    c.getString(c.getColumnIndexOrThrow("talla")),
                    c.getString(c.getColumnIndexOrThrow("marca")),
                    c.getString(c.getColumnIndexOrThrow("color")),
                    c.getDouble(c.getColumnIndexOrThrow("precio")),
                    c.getInt(c.getColumnIndexOrThrow("cantidad")),
                    c.getString(c.getColumnIndexOrThrow("foto"))
                )
            )
        }
        return lista
    }
}