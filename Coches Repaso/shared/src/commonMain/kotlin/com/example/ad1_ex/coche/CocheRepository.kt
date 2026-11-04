package com.example.ad1_ex.coche

interface CocheRepository {
    suspend fun obtenerTodos(): List<Coche>
    suspend fun guardarCoche(coche: Coche)
    suspend fun buscarListaCoche(coche: Coche): List<Coche>
    suspend fun insertar(coche: Coche): List<Coche>

    suspend fun sobrescribir(coche: Coche): List<Coche>
    suspend fun eliminar(id: String): List<Coche>

}

// Función fábrica que devolverá la implementación correspondiente según la plataforma
expect fun getCocheRepository(): CocheRepository