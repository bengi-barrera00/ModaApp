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