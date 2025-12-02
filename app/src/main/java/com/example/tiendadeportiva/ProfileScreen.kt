package com.example.tiendadeportiva

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val usuario = UserManager.usuarioActual.value

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("👤 Mi Perfil", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(32.dp))

        if (usuario != null) {
            // Tarjeta con info del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Nombre:", value = usuario.nombre)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(label = "Correo:", value = usuario.correo)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(label = "Tipo:", value = usuario.tipo)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botón Cerrar Sesión (Rojo)
            Button(
                onClick = {
                    UserManager.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), // Rojo oscuro
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Cerrar Sesión 👋")
            }
        } else {
            Text("No hay sesión iniciada")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value)
    }
}