package com.example.LC_App.nav

sealed class Route(val path: String) {
    object Login : Route("login")
    object Register : Route("register")
    object Home : Route("home")
    object UserMenu : Route("user_menu")
    object UserList : Route("user_list")
    object Sensors : Route("sensors")
    object Developer : Route("developer")
    object Recover : Route("recover")

    object RecoverPassword : Route("recover_password")

    
    // Ruta dinámica para editar usuario
    object EditUser : Route("edit_user/{userId}") {
        fun createRoute(userId: Int) = "edit_user/$userId"
    }
}