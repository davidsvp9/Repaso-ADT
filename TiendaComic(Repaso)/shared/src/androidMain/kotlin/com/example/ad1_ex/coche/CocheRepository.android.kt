package com.example.ad1_ex.coche

import com.example.ad1_ex.MyLog

class AndroidCocheRepository : CocheRepository {

    override suspend fun obtenerTodos(): List<Coche> {
        MyLog.d("AndroidCocheRepository.obtenerTodos")
        return emptyList()
    }
    
    override suspend fun guardarCoche(coche: Coche) {
        MyLog.d("AndroidCocheRepository.guardarCoche: $coche")
    }


    override suspend fun buscarListaCoche(coche: Coche): List<Coche> {
        MyLog.d("AndroidCocheRepository.buscarListaCoche: $coche")
        return emptyList()
    }

    override suspend fun insertar(coche: Coche): List<Coche> {
        MyLog.d("AndroidCocheRepository.insertar: $coche")
        return emptyList()
    }

    override suspend fun sobrescribir(coche: Coche): List<Coche> {
        MyLog.d("AndroidCocheRepository.sobrescribir: $coche")
        return emptyList()
    }

}
actual fun getCocheRepository(): CocheRepository = AndroidCocheRepository()