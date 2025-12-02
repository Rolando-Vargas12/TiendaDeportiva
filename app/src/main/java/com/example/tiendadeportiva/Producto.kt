package com.example.tiendadeportiva

import kotlinx.serialization.Serializable

@Serializable // <- AÑADIMOS ESTA ANOTACIÓN
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int,
    // Cambiamos a String, ya que tu API envía una URL (p.img)
    val img: String
)

// Tu API devuelve un objeto que contiene una lista de productos en el campo 'data'.
// Necesitamos un modelo para leer esa respuesta.
@Serializable
data class ProductoApiResponse(
    val data: List<Producto>
)

// Archivo Producto.kt (Agregar al final)

// Modelo para la solicitud de Login
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

// Modelo para la solicitud de Registro
@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// Modelo para la respuesta del servidor después del Login
@Serializable
data class UserResponse(
    val name: String,
    val email: String,
    val id: String // El token/id que usas en el frontend
)

// Modelo para la solicitud de Login
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

// Modelo para la solicitud de Registro
@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// Modelo para la respuesta del servidor después del Login
@Serializable
data class UserResponse(
    val name: String,
    val email: String,
    val id: String // El token/id que usas en el frontend
)

// Modelo para la solicitud de Venta (Datos enviados al puerto 8082)
@Serializable
data class VentaRequest(
    val cliente: String,
    val total: Double // Usamos Double para el monto
)

// Modelo para la respuesta del servidor después de la venta
@Serializable
data class VentaResponse(
    val id: String // ID de la transacción
)