package com.example.tiendadeportiva

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun CartScreen(onBack: () -> Unit, onPaySuccess: () -> Unit) {
    val carrito = CartManager.productosEnCarrito
    val usuario = UserManager.usuarioActual.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val totalMonto = CartManager.obtenerTotal().toDouble()

    // CORRECCIÓN: Definimos explícitamente que esto devuelve Unit (vacío)
    val handlePagar: () -> Unit = {
        if (carrito.isEmpty()) {
            Toast.makeText(context, "El carrito está vacío", Toast.LENGTH_SHORT).show()
        } else if (usuario == null) {
            Toast.makeText(context, "Inicia sesión para pagar", Toast.LENGTH_LONG).show()
        } else {
            // Guardamos el nombre en una variable segura antes de lanzar la corrutina
            val clienteNombre = usuario.nombre

            scope.launch {
                // Ahora usamos la variable segura
                val result = ApiClient.createVentaService(clienteNombre, totalMonto)

                if (result.isSuccess) {
                    val venta = result.getOrThrow()
                    Toast.makeText(context, "✅ ¡Pago Exitoso! ID: ${venta.id}", Toast.LENGTH_LONG).show()
                    CartManager.limpiar()
                    onPaySuccess()
                } else {
                    Toast.makeText(context, "❌ Error de pago: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                }
            }
            // Este Unit final asegura que la función no devuelva el Job del launch
            Unit
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tu Carrito 🛒", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        if (carrito.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("El carrito está vacío 😢")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(carrito) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, fontWeight = FontWeight.Bold)
                                Text("$${producto.precio}")
                            }
                            IconButton(onClick = { CartManager.remover(producto) }) { Text("❌") }
                        }
                    }
                }
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("$${CartManager.obtenerTotal()}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = handlePagar, modifier = Modifier.fillMaxWidth()) {
                Text("Pagar Ahora")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Seguir comprando")
        }
    }
}