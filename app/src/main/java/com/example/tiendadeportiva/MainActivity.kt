package com.example.tiendadeportiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asegúrate de que el tema de tu aplicación se aplica aquí si estás usando un tema personalizado
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        // --- HOME ---
        composable(Screen.Home.route) {
            HomeScreen(
                onProductClick = { id -> navController.navigate(Screen.Detalle.createRoute(id)) },
                onCartClick = { navController.navigate(Screen.Carrito.route) },
                onUserClick = {
                    if (UserManager.estaLogueado()) navController.navigate(Screen.Perfil.route)
                    else navController.navigate(Screen.Login.route)
                },
                onInfoClick = {
                    navController.navigate(Screen.Nosotros.route)
                }
            )
        }

        // --- DETALLE ---
        composable(Screen.Detalle.route, arguments = listOf(navArgument("productoId") { type = NavType.IntType })) {
            val id = it.arguments?.getInt("productoId") ?: 0
            DetailScreen(productoId = id, onBack = { navController.popBackStack() })
        }

        // --- CARRITO ---
        composable(Screen.Carrito.route) {
            CartScreen(onBack = { navController.popBackStack() }, onPaySuccess = { navController.navigate(Screen.Pagos.route) })
        }

        // --- PAGOS ---
        composable(Screen.Pagos.route) {
            PaymentScreen(onHomeClick = { navController.popBackStack(Screen.Home.route, inclusive = false) })
        }

        // --- LOGIN ---
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.popBackStack(Screen.Home.route, false) },
                onNavigateToRegister = { navController.navigate(Screen.Registro.route) }
            )
        }

        // --- REGISTRO ---
        composable(Screen.Registro.route) {
            RegisterScreen(
                onRegistrationSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // --- PERFIL ---
        composable(Screen.Perfil.route) {
            ProfileScreen(
                onLogout = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        // --- NOSOTROS ---
        composable(Screen.Nosotros.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onUserClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    // 1. Manejo de estado: Lista de productos y si está cargando
    var productos by remember { mutableStateOf(emptyList<Producto>()) }
    var isLoading by remember { mutableStateOf(true) }
    val usuario = UserManager.usuarioActual.value

    // 2. Efecto de Carga (Simula el useEffect de React para llamadas a API)
    LaunchedEffect(Unit) {
        isLoading = true
        productos = ApiClient.getProductos() // <--- ¡LLAMADA REAL A TU BACKEND!
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏆 Tienda Deportiva") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onInfoClick) { Text("ℹ️", fontSize = 22.sp) }
                    IconButton(onClick = onUserClick) { Text(if (usuario != null) "👤✅" else "👤", fontSize = 22.sp) }
                    IconButton(onClick = onCartClick) { Text("🛒", fontSize = 22.sp) }
                }
            )
        }
    ) { padding ->
        // 3. Mostrar estado de carga, error o la lista
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
                Text("Cargando productos...", modifier = Modifier.padding(top = 80.dp))
            }
        } else if (productos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    "No se encontraron productos o el servidor está apagado (Revisa el Logcat).",
                    color = Color.Red,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = Alignment.Center.toString().let { androidx.compose.ui.text.style.TextAlign.Center }
                )
            }
        } else {
            // Mostrar la lista con los productos de la API
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(productos) { producto ->
                    ProductCard(producto = producto, onItemClick = { onProductClick(producto.id) })
                }
            }
        }
    }
}