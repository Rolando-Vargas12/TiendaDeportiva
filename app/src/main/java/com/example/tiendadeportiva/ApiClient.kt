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
import android.util.Log

object ApiClient {
    // CAMBIO IMPORTANTE: Usamos tu IP real
    private const val MI_IP_PC = "192.168.100.8"

    private const val BASE_URL_PRODUCTOS = "http://$MI_IP_PC:8081"
    private const val BASE_URL_USERS = "http://$MI_IP_PC:8080"
    private const val BASE_URL_SALES = "http://$MI_IP_PC:8082"

    // Rutas corregidas (sin el /api duplicado)
    private const val PRODUCTOS_ENDPOINT = "/api/productos"
    private const val USERS_ENDPOINT = "/api/users"
    private const val SALES_ENDPOINT = "/api/ventas"

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
    }

    suspend fun getProductos(): List<Producto> {
        return try {
            val url = "$BASE_URL_PRODUCTOS$PRODUCTOS_ENDPOINT"
            Log.d("API_CLIENT", "Conectando a: $url")

            val response = client.get(url)

            if (response.status == HttpStatusCode.OK) {
                val apiResponse = response.body<ProductoApiResponse>()
                Log.d("API_CLIENT", "Productos recibidos: ${apiResponse.data.size}")
                apiResponse.data
            } else {
                Log.e("API_CLIENT", "Error HTTP: ${response.status}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("API_CLIENT", "Error de conexión (Revisa IP y Firewall): ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    suspend fun getProductoPorId(id: Int): Producto? {
        return try {
            val response = client.get("$BASE_URL_PRODUCTOS$PRODUCTOS_ENDPOINT/$id")

            if (response.status == HttpStatusCode.OK) {
                response.body<ProductoSingleResponse>().data
            } else {
                Log.e("API_CLIENT", "Error al buscar producto $id: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("API_CLIENT", "Error de conexión al buscar detalle: ${e.message}")
            null
        }
    }

    // ... el resto de funciones (ventas, login, etc.) ...

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
                    Result.failure(Exception("Error: ${response.bodyAsText()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error Ventas: ${e.message}"))
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
                Result.failure(Exception("Error Login: ${e.message}"))
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
                    Result.failure(Exception("Error Registro"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error Registro: ${e.message}"))
            }
        }
    }
    suspend fun crearProducto(producto: ProductoRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // Hacemos un POST a /api/productos
                val response = client.post("$BASE_URL_PRODUCTOS$PRODUCTOS_ENDPOINT") {
                    contentType(ContentType.Application.Json)
                    setBody(producto)
                }
                if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {
                    Result.success(Unit)
                } else {
                    val error = response.bodyAsText()
                    Result.failure(Exception("Error al crear: $error"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }
}
