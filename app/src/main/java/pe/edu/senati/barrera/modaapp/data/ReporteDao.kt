package pe.edu.senati.barrera.modaapp.data

import android.content.Context
import pe.edu.senati.barrera.modaapp.model.Fila
import pe.edu.senati.barrera.modaapp.model.ResumenMes

class ReporteDao(ctx: Context) {
    private val helper = DBHelper(ctx)

    fun atendidosDelMes(): ResumenMes {
        helper.readableDatabase.rawQuery(
            """SELECT COUNT(*), IFNULL(SUM(total), 0) FROM pedido
               WHERE estado = 'ATENDIDO'
               AND strftime('%Y-%m', fecha_atencion) = strftime('%Y-%m', 'now', 'localtime')""",
            null
        ).use { c ->
            c.moveToFirst()
            return ResumenMes(c.getInt(0), c.getDouble(1))
        }
    }

    fun stockPorPrenda(): List<Fila> {
        val lista = mutableListOf<Fila>()
        helper.readableDatabase.rawQuery(
            "SELECT modelo, talla, color, cantidad FROM ropa ORDER BY cantidad, modelo", null
        ).use { c ->
            while (c.moveToNext()) {
                val cant = c.getInt(3)
                lista.add(
                    Fila(
                        titulo = c.getString(0),
                        detalle = "Talla ${c.getString(1) ?: "-"} · ${c.getString(2) ?: "-"}",
                        derecha = "Stock: $cant",
                        alerta = cant <= 3
                    )
                )
            }
        }
        return lista
    }

    fun clientesConPedidos(): List<Fila> {
        val lista = mutableListOf<Fila>()
        helper.readableDatabase.rawQuery(
            """SELECT c.nombres, c.apellidos, c.telefono, COUNT(p.id)
               FROM cliente c LEFT JOIN pedido p ON p.id_cliente = c.id
               GROUP BY c.id ORDER BY c.apellidos, c.nombres""", null
        ).use { c ->
            while (c.moveToNext()) {
                val n = c.getInt(3)
                lista.add(
                    Fila(
                        titulo = "${c.getString(0)} ${c.getString(1)}",
                        detalle = "Tel: ${c.getString(2)}",
                        derecha = if (n == 1) "1 pedido" else "$n pedidos"
                    )
                )
            }
        }
        return lista
    }
}