package pe.edu.senati.barrera.modaapp.model

data class ItemCarrito(val ropa: Ropa, var cantidad: Int) {
    val subtotal: Double get() = ropa.precio * cantidad
}

object Carrito {
    val items = mutableListOf<ItemCarrito>()

    fun cantidadDe(idRopa: Int): Int =
        items.firstOrNull { it.ropa.id == idRopa }?.cantidad ?: 0

    fun agregar(ropa: Ropa, cantidad: Int) {
        val existente = items.firstOrNull { it.ropa.id == ropa.id }
        if (existente != null) existente.cantidad += cantidad
        else items.add(ItemCarrito(ropa, cantidad))
    }

    fun quitar(item: ItemCarrito) {
        items.remove(item)
    }

    fun vaciar() = items.clear()

    fun total(): Double = items.sumOf { it.subtotal }

    fun unidades(): Int = items.sumOf { it.cantidad }
}