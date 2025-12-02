package com.example.tiendadeportiva

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentScreen(onHomeClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Permite hacer scroll si la pantalla es chica
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mensaje de Éxito (Simulando el alert-success de React)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD4EDDA)), // Verde claro
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "✅ ¡Gracias por tu compra! Tu pedido ha sido procesado.",
                modifier = Modifier.padding(16.dp),
                color = Color(0xFF155724), // Verde oscuro
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("💳 Métodos de Pago", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Aceptamos las siguientes formas de pago:", color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de Métodos (Igual que tu <ul> en React)
        MetodoPagoItem("💳 Tarjeta de crédito y débito", "Visa, MasterCard, Amex.")
        MetodoPagoItem("🏦 Transferencia bancaria", "Te enviaremos los datos al correo.")
        MetodoPagoItem("💵 Efectivo", "Solo para retiro en tienda.")
        MetodoPagoItem("🌐 Webpay / MercadoPago", "Plataformas seguras.")

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Volver al Inicio
        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🛍️ Volver a comprar")
        }
    }
}

@Composable
fun MetodoPagoItem(titulo: String, desc: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold)
            Text(desc, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}