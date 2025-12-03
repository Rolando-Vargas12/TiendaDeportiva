package com.example.tiendadeportiva

import kotlinx.serialization.Serializable

// --- MODELO DE PRODUCTO ---
@Serializable
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val img: String
)

@Serializable
data class ProductoApiResponse(
    val data: List<Producto>
)

// --- MODELO DE USUARIO (PARA LA APP) ---
// ¡Esta es la clase que faltaba!
data class Usuario(
    val nombre: String,
    val correo: String,
    val tipo: String
)

// --- MODELOS DE API (LOGIN Y REGISTRO) ---

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class UserResponse(
    val name: String,
    val email: String,
    val id: String
)

// --- MODELOS DE VENTAS ---

@Serializable
data class VentaRequest(
    val cliente: String,
    val total: Double
)

@Serializable
data class VentaResponse(
    val id: String
)

@Serializable
data class ProductoSingleResponse(
    val data: Producto
)

@Serializable
data class ProductoRequest(
    val codigo: String,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val img: String,
    val descripcion: String = "Producto creado desde la App"
)