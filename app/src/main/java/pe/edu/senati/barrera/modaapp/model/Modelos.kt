package pe.edu.senati.barrera.modaapp.model

data class Usuario(
    val id: Int,
    val usuario: String,
    val rol: String
)

data class Categoria(
    val id: Int,
    val nombre: String
)

data class Ropa(
    val id: Int = 0,
    val modelo: String,
    val idCategoria: Int,
    val talla: String? = null,
    val marca: String? = null,
    val color: String? = null,
    val precio: Double,
    val cantidad: Int,
    val foto: String? = null
)

data class Cliente(
    val id: Int,
    val telefono: String,
    val nombres: String,
    val apellidos: String
)
data class LineaPedido(
    val modelo: String, val talla: String?, val color: String?,
    val cantidad: Int, val subtotal: Double, val foto: String?
)

data class PedidoResumen(
    val id: Long, val fecha: String, val total: Double, val estado: String,
    val nombres: String, val apellidos: String, val telefono: String,
    val lineas: List<LineaPedido>
)


data class Fila(
    val titulo: String, val detalle: String, val derecha: String,
    val alerta: Boolean = false, val id: Long = 0L
)

data class ResumenMes(val pedidos: Int, val monto: Double)