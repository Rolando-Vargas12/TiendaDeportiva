package com.example.tiendadeportiva

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// --- IMPORTACIONES NUEVAS PARA EL ICONO ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Sobre Nosotros", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("📜 Historia")
        Text("Tienda Deportiva nació en 2015 con el objetivo de acercar los mejores accesorios y productos deportivos a todos los amantes del deporte en Chile. Hemos trabajado para ofrecer calidad y precios accesibles.")

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("🎯 Misión")
        Text("Nuestra misión es fomentar el deporte y el bienestar, proporcionando productos confiables y asesoría personalizada para que cada cliente encuentre lo que necesita.")

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("👥 Equipo")
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // REEMPLAZAMOS el 'Image(painter = painterResource(...))' incompatible por 'Icon'
                Icon(
                    imageVector = Icons.Default.Person, // Icono estándar de Persona
                    contentDescription = "Rolando Vargas",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary // Le damos color
                )
                // -------------------------------------------------------------
                Spacer(modifier = Modifier.height(8.dp))
                Text("Rolando Vargas", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Fundador y Gerente General", color = MaterialTheme.colorScheme.secondary)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al Inicio")
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
}