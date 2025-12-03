package com.example.tiendadeportiva

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.call.body
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiClient {
    private const val BASE_URL_PRODUCTOS = "http://10.0.2.2:8081"
    private const val BASE_URL_USERS = "http://10.0.2.2:8080"
    private const val BASE_URL_SALES = "http://10.0.2.2:8082"

    private const val PRODUCTOS_ENDPOINT = "/api/api/productos" // Ajustado al context-path
    private const val USERS_ENDPOINT = "/api/users"
    private const val SALES_ENDPOINT = "/api/api/ventas" // Ajustado al context-path

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getProductos(): List<Producto> {
        return try {
            // Concatenamos URL Base + Endpoint
            val response = client.get("$BASE_URL_PRODUCTOS$PRODUCTOS_ENDPOINT")
            if (response.status == HttpStatusCode.OK) {
                // Mapeamos la respuesta usando la clase envoltorio que creamos en Producto.kt
                response.body<ProductoApiResponse>().data
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createVentaService(cliente: String, total: Double): Result<VentaResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = VentaRequest(cliente, total)
                val response = client.post("$BASE_URL_SALES$SALES_ENDPOINT") {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
                if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {
                    val ventaData = response.body<VentaResponse>()
                    Result.success(ventaData)
                } else {
                    val errorBody = response.bodyAsText()
                    Result.failure(Exception("Error al procesar el pago: $errorBody"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión al microservicio de Ventas: ${e.message}"))
            }
        }
    }

    suspend fun loginService(correo: String, pass: String): Result<UserResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = LoginRequest(correo, pass)
                val response = client.post("$BASE_URL_USERS$USERS_ENDPOINT/login") {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
                if (response.status == HttpStatusCode.OK) {
                    Result.success(response.body<UserResponse>())
                } else {
                    Result.failure(Exception("Credenciales inválidas"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    suspend fun registerService(nombre: String, correo: String, pass: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = RegisterRequest(nombre, correo, pass)
                val response = client.post("$BASE_URL_USERS$USERS_ENDPOINT/register") {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
                if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error en el registro"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión al registrarse."))
            }
        }
    }
}
