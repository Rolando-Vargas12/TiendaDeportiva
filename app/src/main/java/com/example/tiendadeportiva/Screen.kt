package com.example.tiendadeportiva

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Carrito : Screen("carrito")
    object Pagos : Screen("pagos")
    object Login : Screen("login")
    object Perfil : Screen("perfil")
    object Nosotros : Screen("nosotros") // <--- ¡VERIFICA QUE ESTA LÍNEA ESTÉ!
    object Registro : Screen("registro")
    object Detalle : Screen("detalle/{productoId}") {
        fun createRoute(id: Int) = "detalle/$id"
    }
}