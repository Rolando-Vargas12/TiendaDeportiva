package com.example.tiendadeportiva

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(productoId: Int, onBack: () -> Unit) {
    var producto by remember { mutableStateOf<Producto?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(productoId) {
        producto = ApiClient.getProductoPorId(productoId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Cargando...") },
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

                val context = LocalContext.current
                val esUrlRemota = producto!!.img.startsWith("http")

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (esUrlRemota) {
                        // Caso Admin (Internet)
                        SubcomposeAsyncImage(
                            model = producto!!.img,
                            contentDescription = producto!!.nombre,
                            contentScale = ContentScale.Fit, // <--- CAMBIO: Se ve entera
                            modifier = Modifier.fillMaxWidth().height(300.dp).padding(bottom = 16.dp),
                            loading = { CircularProgressIndicator() },
                            error = { Text("🚫 Imagen no disponible", color = Color.Red) }
                        )
                    } else {
                        // Caso Base (Local)
                        val imageResId = remember(producto!!.img) {
                            val id = context.resources.getIdentifier(producto!!.img, "drawable", context.packageName)
                            if (id == 0) android.R.drawable.ic_menu_report_image else id
                        }
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = producto!!.nombre,
                            contentScale = ContentScale.Fit, // <--- CAMBIO: Se ve entera
                            modifier = Modifier.fillMaxWidth().height(300.dp).padding(bottom = 16.dp)
                        )
                    }

                    Text(producto!!.nombre, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Precio: $${producto!!.precio}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción con fallback por si es nula
                    Text("Descripción: ${producto!!.descripcion ?: "Sin descripción disponible."}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(32.dp))

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
                Text("Producto no encontrado", modifier = Modifier.align(Alignment.Center), color = Color.Red)
            }
        }
    }
}