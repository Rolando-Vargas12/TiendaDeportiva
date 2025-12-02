package com.example.tiendadeportiva

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Usamos la misma clase Usuario que definimos en Producto.kt
// data class Usuario...

object UserManager {
    val usuarioActual = mutableStateOf<Usuario?>(null)

    // FUNCIÓN DE LOGIN ACTUALIZADA PARA USAR LA API
    suspend fun login(correo: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val result = ApiClient.loginService(correo, pass)

            result.onSuccess { userData ->
                // 2. Lógica simplificada de ROLES (Igual que en tu React)
                val rol = if (userData.email.contains("admin") || userData.email.contains("profesor")) "Administrador" else "Cliente"

                usuarioActual.value = Usuario(userData.name, userData.email, rol)
                return@withContext Result.success(Unit)
            }
            return@withContext Result.failure(result.exceptionOrNull() ?: Exception("Login fallido"))
        }
    }

    // FUNCIÓN DE REGISTRO ACTUALIZADA PARA USAR LA API
    suspend fun register(nombre: String, correo: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            ApiClient.registerService(nombre, correo, pass)
        }
    }

    fun logout() {
        usuarioActual.value = null
    }

    fun estaLogueado(): Boolean {
        return usuarioActual.value != null
    }
}