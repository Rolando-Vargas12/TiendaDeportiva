package com.example.tiendadeportiva

import androidx.compose.runtime.mutableStateListOf

// Esto es equivalente a tu CartContext
object CartManager {
    // Esta es la lista "reactiva". Si cambia, la pantalla se actualiza sola.
    val productosEnCarrito = mutableStateListOf<Producto>()

    fun agregar(producto: Producto) {
        productosEnCarrito.add(producto)
    }

    fun remover(producto: Producto) {
        productosEnCarrito.remove(producto)
    }

    fun limpiar() {
        productosEnCarrito.clear()
    }

    fun obtenerTotal(): Double {
        return productosEnCarrito.sumOf { it.precio }
    }
}