package pe.edu.senati.barrera.modaapp.data

import android.content.ContentValues
import android.content.Context
import pe.edu.senati.barrera.modaapp.model.ItemCarrito
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
}