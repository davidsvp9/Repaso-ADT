package com.example.ad1_ex.libro

interface LibroRepository {
    suspend fun obtenerTodos(): List<Libro>
    suspend fun guardarLibro(libro: Libro)
    suspend fun buscarListaLibros(libro: Libro): List<Libro>
    suspend fun insertar(libro: Libro): List<Libro>

    suspend fun sobrescribir(libro: Libro): List<Libro>
    suspend fun eliminar(id: String): List<Libro>

}

// Función fábrica que devolverá la implementación correspondiente según la plataforma
expect fun getLibroRepository(): LibroRepository