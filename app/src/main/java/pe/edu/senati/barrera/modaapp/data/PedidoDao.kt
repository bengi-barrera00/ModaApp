package pe.edu.senati.barrera.modaapp.data

import android.content.ContentValues
import android.content.Context
import pe.edu.senati.barrera.modaapp.model.Fila
import pe.edu.senati.barrera.modaapp.model.ItemCarrito
import pe.edu.senati.barrera.modaapp.model.LineaPedido
import pe.edu.senati.barrera.modaapp.model.PedidoResumen
import pe.edu.senati.barrera.modaapp.util.Fechas

class PedidoDao(ctx: Context) {
    private val helper = DBHelper(ctx)

    fun registrar(idCliente: Int, items: List<ItemCarrito>): Long {
        val db = helper.writableDatabase
        db.beginTransaction()
        try {
            val cabecera = ContentValues().apply {
                put("id_cliente", idCliente)
                put("fecha", Fechas.ahora())
                put("total", items.sumOf { it.subtotal })
                put("estado", "PENDIENTE")
            }
            val idPedido = db.insertOrThrow("pedido", null, cabecera)

            for (item in items) {
                val linea = ContentValues().apply {
                    put("id_pedido", idPedido)
                    put("id_ropa", item.ropa.id)
                    put("cantidad", item.cantidad)
                    put("precio_unit", item.ropa.precio)
                    put("subtotal", item.subtotal)
                }
                db.insertOrThrow("detalle_pedido", null, linea)
            }

            db.setTransactionSuccessful()
            return idPedido
        } finally {
            db.endTransaction()
        }
    }

    private fun lineas(idPedido: Long): List<LineaPedido> {
        val lista = mutableListOf<LineaPedido>()
        helper.readableDatabase.rawQuery(
            """SELECT r.modelo, r.talla, r.color, d.cantidad, d.subtotal, r.foto
               FROM detalle_pedido d JOIN ropa r ON r.id = d.id_ropa
               WHERE d.id_pedido = ?""", arrayOf(idPedido.toString())
        ).use { c ->
            while (c.moveToNext()) lista.add(
                LineaPedido(
                    c.getString(0), c.getString(1), c.getString(2),
                    c.getInt(3), c.getDouble(4), c.getString(5)
                )
            )
        }
        return lista
    }

    fun obtenerResumen(idPedido: Long): PedidoResumen? {
        helper.readableDatabase.rawQuery(
            """SELECT p.id, p.fecha, p.total, p.estado, c.nombres, c.apellidos, c.telefono
               FROM pedido p JOIN cliente c ON c.id = p.id_cliente
               WHERE p.id = ?""", arrayOf(idPedido.toString())
        ).use { c ->
            if (!c.moveToFirst()) return null
            return PedidoResumen(
                c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3),
                c.getString(4), c.getString(5), c.getString(6), lineas(idPedido)
            )
        }
    }

    fun listarPorEstado(estado: String): List<Fila> {
        val lista = mutableListOf<Fila>()
        helper.readableDatabase.rawQuery(
            """SELECT p.id, c.nombres || ' ' || c.apellidos, p.fecha, p.total
               FROM pedido p JOIN cliente c ON c.id = p.id_cliente
               WHERE p.estado = ?
               ORDER BY p.fecha DESC, p.id DESC""", arrayOf(estado)
        ).use { c ->
            while (c.moveToNext()) lista.add(
                Fila(
                    id = c.getLong(0),
                    titulo = c.getString(1),
                    detalle = "Pedido #${c.getLong(0)} · ${c.getString(2).take(16)}",
                    derecha = "S/ %.2f".format(c.getDouble(3))
                )
            )
        }
        return lista
    }

    fun atender(idPedido: Long): String? {
        val db = helper.writableDatabase
        db.beginTransaction()
        try {
            var sinStock: String? = null
            db.rawQuery(
                """SELECT r.modelo, r.cantidad, d.cantidad
                   FROM detalle_pedido d JOIN ropa r ON r.id = d.id_ropa
                   WHERE d.id_pedido = ?""", arrayOf(idPedido.toString())
            ).use { c ->
                while (c.moveToNext() && sinStock == null) {
                    if (c.getInt(1) < c.getInt(2)) sinStock = c.getString(0)
                }
            }
            if (sinStock != null) return sinStock

            db.execSQL(
                """UPDATE ropa SET cantidad = cantidad - (
                       SELECT d.cantidad FROM detalle_pedido d
                       WHERE d.id_pedido = ? AND d.id_ropa = ropa.id)
                   WHERE id IN (SELECT id_ropa FROM detalle_pedido WHERE id_pedido = ?)""",
                arrayOf(idPedido, idPedido)
            )
            db.execSQL(
                "UPDATE pedido SET estado = 'ATENDIDO', fecha_atencion = ? " +
                        "WHERE id = ? AND estado = 'PENDIENTE'",
                arrayOf(Fechas.ahora(), idPedido)
            )
            db.setTransactionSuccessful()
            return null
        } finally {
            db.endTransaction()
        }
    }
}