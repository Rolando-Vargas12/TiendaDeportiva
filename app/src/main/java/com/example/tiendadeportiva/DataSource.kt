package com.example.tiendadeportiva

object DataSource {
    // CORREGIDO: Usamos 'img' (String) para que coincida con el modelo de la API
    val productos = listOf(
        Producto(id = 1, nombre = "Balón de Fútbol", precio = 19990, img = "https://mockapi.com/img/ball.jpg"),
        Producto(id = 2, nombre = "Zapatillas Running", precio = 59990, img = "https://mockapi.com/img/shoe.jpg"),
        Producto(id = 3, nombre = "Guantes de Boxeo", precio = 29990, img = "https://mockapi.com/img/glove.jpg")
    )
}