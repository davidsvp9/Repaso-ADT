package com.example.ad1_ex.libro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ad1_ex.MyLog
import kotlinx.coroutines.launch

class LibroViewModel : ViewModel() {

    private val libroRepository = getLibroRepository()

    var listaLibros by mutableStateOf<List<Libro>>(emptyList())
        private set


    var id by mutableStateOf("");private set

    var isbn by mutableStateOf("");private set

    fun isbnChanged(nuevo: String) { isbn = nuevo }

    var titulo by mutableStateOf("");private set

    fun tituloChanged(nuevo: String) { titulo = nuevo }

    var autor by mutableStateOf("");private set

    fun autorChanged(nuevo: String) { autor = nuevo }

    var paginas by mutableStateOf("");private set

    fun paginasChanged(nuevo: String) { paginas = nuevo }

    var precio by mutableStateOf("");private set

    fun precioChanged(nuevo: String) { precio = nuevo }

    var disponible by mutableStateOf(false);private set

    fun disponibleChanged(nuevo: Boolean) { disponible = nuevo }



    fun obtenerTodos() {
        viewModelScope.launch { // 1. Inicia una corrutina en segundo plano
            listaLibros = libroRepository.obtenerTodos() // 2. Hace la consulta (operación suspendida o pesada)
        }
    }



    fun insertarLibro() {
        MyLog.d("viewModel.insertarLibro()")
        viewModelScope.launch {
            val paginasInt=paginas.toIntOrNull()?:0
            val precioDouble=precio.toDoubleOrNull()?:0.0
            val nuevo = Libro(
                isbn = isbn,
                titulo = titulo,
                autor = autor,
                paginas = paginasInt,
                precio = precioDouble,
                disponible = disponible
            )
            libroRepository.insertar(nuevo)

            isbn = ""
            titulo = ""
            autor = ""
            paginas = ""
            precio = ""
            disponible = false

            obtenerTodos() // Actualiza la lista automáticamente al insertar
        }
    }


    fun eliminar(idEliminar: String) {
        MyLog.d("viewModel.eliminar ID: $idEliminar")
        viewModelScope.launch {
            listaLibros = libroRepository.eliminar(idEliminar)
        }
    }

    fun cargarLibroParaEditar(libro: Libro) {
        MyLog.d("viewModel.cargarCocheParaEditar(${libro.id})")
        // Asumiendo que tu clase Coche tiene un campo 'id'.
        // Si tu ID es un Int en lugar de String, ajústalo según corresponda (ej. coche.id.toString())
        id = libro.id
        isbn = libro.isbn
        titulo = libro.titulo
        autor = libro.autor
        paginas = libro.paginas.toString()
        precio = libro.precio.toString()
        disponible = libro.disponible

    }


    fun limpiarFormulario() {
        id = ""
        isbn = ""
        titulo = ""
        autor = ""
        paginas = ""
        precio = ""
        disponible = false

    }

    fun actualizarLibro() {
        MyLog.d("viewModel.actualizarCoche()")
        viewModelScope.launch {
            val libroEditado = Libro(
                id = id,
                isbn = isbn,
                titulo = titulo,
                autor = autor,
                paginas = paginas.toInt(),
                precio = precio.toDouble(),
                disponible = disponible
            )
            MyLog.d("Actualizando libro: $libroEditado")
            libroRepository.sobrescribir(libroEditado)
            obtenerTodos() // Refresca la lista
        }
    }

    fun filtrar(criterio: String) {
        MyLog.d("viewModel.filtrar por autor: $criterio")
        viewModelScope.launch {
            if (criterio.isBlank()) {
                obtenerTodos()
            } else {
                // Creamos el objeto indicando que filtre únicamente por autor
                val libroFiltro = Libro(
                    isbn= "",
                    titulo = "",
                    autor = criterio.trim(),
                    paginas = 0,
                    precio = 0.0,
                    disponible = false
                )
                listaLibros = libroRepository.buscarListaLibros(libroFiltro)
            }
        }
    }



}



