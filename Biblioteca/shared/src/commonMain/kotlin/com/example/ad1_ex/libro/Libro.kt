package com.example.ad1_ex.libro

data class Libro(
    val id: String = "",
    val isbn: String = "",
    val titulo: String ="",
    val autor: String="",
    val paginas: Int=0,
    val precio: Double=0.0,
    val disponible: Boolean = false
)