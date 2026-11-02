package com.example.ad1_ex.comic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ad1_ex.MyLog
import com.example.ad1_ex.coche.Coche
import com.example.ad1_ex.coche.getCocheRepository
import kotlinx.coroutines.launch

class ComicViewModel : ViewModel() {
    private val comicRepository = getComicRepository()

    // Estado para almacenar la lista de coches
    var listaComics by mutableStateOf<List<Comic>>(emptyList())
        private set

    // Estados para el formulario de creación/edición
    // matricula
    var titulo by mutableStateOf("");private set
    fun tituloChanged(nuevo: String) { titulo = nuevo }

    // modelo
    var precio by mutableStateOf("");private set
    fun precioChanged(nuevo: String) { precio = nuevo }

    // color
    var stock by mutableStateOf("");private set
    fun stockChanged(nuevo: String) { stock = nuevo }

    // Obtener listado completo
    fun obtenerTodos() {
        viewModelScope.launch { // 1. Inicia una corrutina en segundo plano
            listaComics = comicRepository.obtenerTodos() // 2. Hace la consulta (operación suspendida o pesada)
        }
    }

    // Insertar un nuevo coche con los valores del estado
    fun insertarComic() {
        MyLog.d("viewModel.insertarComic()")
        viewModelScope.launch {
            val precioDouble=precio.toDoubleOrNull() ?: 0.0
            val stockInt=stock.toIntOrNull() ?: 0
            val nuevo = Comic(
                titulo = titulo,
                precio = precioDouble,
                stock = stockInt
            )
            comicRepository.insertar(nuevo)

            titulo=""
            precio=""
            stock=""

            obtenerTodos() // Actualiza la lista automáticamente al insertar
        }
    }


    fun eliminarComic(id:String){
        viewModelScope.launch {
            comicRepository.eliminar(id)
            obtenerTodos()
        }
    }
}



