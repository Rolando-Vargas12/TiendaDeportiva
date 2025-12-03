package com.example.tiendadeportiva

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(productoId: Int, onBack: () -> Unit) {
    // CAMBIO: Estado para guardar el producto que viene de internet
    var producto by remember { mutableStateOf<Producto?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // CAMBIO: Efecto que se ejecuta al entrar para buscar los datos
    LaunchedEffect(productoId) {
        producto = ApiClient.getProductoPorId(productoId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Cargando...") }, // Muestra el nombre real o cargando
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (producto != null) {
                // Si el producto llegó bien, mostramos su info
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubcomposeAsyncImage(
                        model = coil.request.ImageRequest.Builder(LocalContext.current)
                            .data(producto!!.img)
                            .listener(
                                onStart = { Log.d("DEBUG_IMG", "Intentando cargar: '${producto!!.img}'") },
                                onError = { _, result ->
                                    Log.e("DEBUG_IMG", "❌ Error de carga: ${result.throwable.message}")
                                    result.throwable.printStackTrace()
                                },
                                onSuccess = { _, _ -> Log.d("DEBUG_IMG", "✅ Imagen cargada correctamente") }
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = producto!!.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(300.dp).padding(bottom = 16.dp),
                        loading = {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        },
                        error = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🚫 Imagen no disponible", color = Color.Red)
                                // Opcional: Mostrar la URL que falló en pantalla chica para ver si tiene basura
                                Text(producto!!.img, fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    )


                    Text(producto!!.nombre, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Precio: $${producto!!.precio}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Nota: Tu API actual no devuelve 'descripcion', así que dejamos un texto fijo por ahora
                    Text("Descripción: Producto deportivo de alta calidad.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(32.dp))

                    val context = LocalContext.current
                    Button(
                        onClick = {
                            CartManager.agregar(producto!!)
                            Toast.makeText(context, "${producto!!.nombre} agregado al carrito!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("🛒 Agregar al Carrito", fontSize = 18.sp)
                    }
                }
            } else {
                Text("Producto no encontrado en el servidor", modifier = Modifier.align(Alignment.Center), color = Color.Red)
            }
        }
    }
}