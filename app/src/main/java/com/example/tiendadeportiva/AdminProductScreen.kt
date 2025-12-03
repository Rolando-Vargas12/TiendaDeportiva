package com.example.tiendadeportiva

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun AdminProductScreen(onBack: () -> Unit) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var imgUrl by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🛠️ Panel de Admin", fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
        Text("Agregar Nuevo Producto")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código único (Ej: PROD-001)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Producto") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock (Cantidad)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = imgUrl, onValueChange = { imgUrl = it }, label = { Text("URL de la Imagen") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (codigo.isNotEmpty() && nombre.isNotEmpty() && precio.isNotEmpty() && stock.isNotEmpty() && imgUrl.isNotEmpty()) {
                    isSaving = true
                    scope.launch {
                        val nuevoProducto = ProductoRequest(
                            codigo = codigo,
                            nombre = nombre,
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            cantidad = stock.toIntOrNull() ?: 0,
                            img = imgUrl
                        )

                        val result = ApiClient.crearProducto(nuevoProducto)
                        isSaving = false

                        if (result.isSuccess) {
                            Toast.makeText(context, "✅ Producto Creado!", Toast.LENGTH_LONG).show()
                            onBack() // Volver al perfil
                        } else {
                            Toast.makeText(context, "❌ Error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (isSaving) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            else Text("Guardar Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("Cancelar") }
    }
}