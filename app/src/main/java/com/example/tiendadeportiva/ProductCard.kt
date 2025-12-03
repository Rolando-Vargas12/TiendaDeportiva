package com.example.tiendadeportiva

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage // <--- ¡OJO! Cambiamos a SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(producto: Producto, onItemClick: () -> Unit) {
    Card(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // CORRECCIÓN: Usamos SubcomposeAsyncImage para soportar 'loading' y 'error' personalizados
            SubcomposeAsyncImage(
                model = producto.img,
                contentDescription = producto.nombre,
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop,
                // Ahora sí podemos usar Composables aquí dentro
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.padding(30.dp))
                    }
                },
                error = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("🖼️ Error", modifier = Modifier.padding(16.dp))
                    }
                }
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "$${producto.precio}", color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))

                // He vinculado el botón a la misma acción que la tarjeta para que sea útil
                Button(onClick = onItemClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver Detalles")
                }
            }
        }
    }
}