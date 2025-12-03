package com.example.tiendadeportiva

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(producto: Producto, onItemClick: () -> Unit) {
    val context = LocalContext.current

    // Detectamos si es URL o archivo local
    val esUrlRemota = producto.img.startsWith("http")

    Card(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if (esUrlRemota) {
                // Caso Admin (Internet)
                SubcomposeAsyncImage(
                    model = producto.img,
                    contentDescription = producto.nombre,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Fit, // <--- CAMBIO: Se ve entera
                    loading = { Box(contentAlignment = Alignment.Center) { CircularProgressIndicator() } },
                    error = { Box(contentAlignment = Alignment.Center) { Text("🚫 URL Rota") } }
                )
            } else {
                // Caso Base (Local)
                val imageResId = remember(producto.img) {
                    val id = context.resources.getIdentifier(producto.img, "drawable", context.packageName)
                    if (id == 0) android.R.drawable.ic_menu_report_image else id
                }
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = producto.nombre,
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Fit // <--- CAMBIO: Se ve entera
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "$${producto.precio}", color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onItemClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver Detalles")
                }
            }
        }
    }
}