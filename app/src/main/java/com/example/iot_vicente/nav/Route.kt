package com.example.iot_vicente.nav

enum class Route(val path: String) {
    Login("login"),
    Register("register"),
    Home("home"),
    UserMenu("user_menu"),
    UserList("user_list"), // Agregado para el listado de usuarios
    Sensors("sensors"),
    Developer("developer"),
    Recover("recover")
}