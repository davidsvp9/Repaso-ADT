package com.example.ad1_ex.comic

interface ComicRepository {
    suspend fun obtenerTodos(): List<Comic>
    suspend fun guardarComic(comic: Comic)
    suspend fun buscarListaComic(comic: Comic): List<Comic>
    suspend fun insertar(comic: Comic): List<Comic>
    suspend fun sobrescribir(comic: Comic): List<Comic>
    suspend fun eliminar(id: String): List<Comic>
}

// Función fábrica que devolverá la implementación correspondiente según la plataforma
expect fun getComicRepository(): ComicRepository
