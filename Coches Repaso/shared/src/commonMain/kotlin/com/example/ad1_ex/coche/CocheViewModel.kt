package com.example.ad1_ex.coche

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ad1_ex.MyLog
import kotlinx.coroutines.launch

class CocheViewModel : ViewModel() {
    private val cocheRepository = getCocheRepository()

    // Estado para almacenar la lista de coches
    var listaCoches by mutableStateOf<List<Coche>>(emptyList())
        private set

    // Estados para el formulario de creación/edición
    // matricula
    var id by mutableStateOf("");private set

    // matricula
    var matricula by mutableStateOf("");private set
    fun matriculaChanged(nuevo: String) { matricula = nuevo }

    // modelo
    var modelo by mutableStateOf("");private set
    fun modeloChanged(nuevo: String) { modelo = nuevo }

    // color
    var color by mutableStateOf("");private set
    fun colorChanged(nuevo: String) { color = nuevo }

    // Obtener listado completo
    fun obtenerTodos() {
        viewModelScope.launch { // 1. Inicia una corrutina en segundo plano
            listaCoches = cocheRepository.obtenerTodos() // 2. Hace la consulta (operación suspendida o pesada)
        }
    }

    // Insertar un nuevo coche con los valores del estado
    fun insertarCoche() {
        MyLog.d("viewModel.insertarCoche()")
        viewModelScope.launch {
            val nuevo = Coche(
                matricula = matricula,
                modelo = modelo,
                color = color
            )
            cocheRepository.insertar(nuevo)
            obtenerTodos() // Actualiza la lista automáticamente al insertar
        }
    }

    fun eliminar(idEliminar: String) {
        MyLog.d("viewModel.eliminar ID: $idEliminar")
        viewModelScope.launch {
            listaCoches = cocheRepository.eliminar(idEliminar)
        }
    }

    // Carga los datos del coche en los campos del formulario al abrir la ventana de editar
    fun cargarCocheParaEditar(coche: Coche) {
        MyLog.d("viewModel.cargarCocheParaEditar(${coche.id})")
        // Asumiendo que tu clase Coche tiene un campo 'id'.
        // Si tu ID es un Int en lugar de String, ajústalo según corresponda (ej. coche.id.toString())
        id = coche.id
        matricula = coche.matricula
        modelo = coche.modelo
        color = coche.color
    }

    // Limpiar el formulario (útil al entrar a Insertar para que no queden datos anteriores)
    fun limpiarFormulario() {
        id = ""
        matricula = ""
        modelo = ""
        color = ""
    }

    // Actualizar / Sobrescribir el coche existente
    fun actualizarCoche() {
        MyLog.d("viewModel.actualizarCoche()")
        viewModelScope.launch {
            val cocheEditado = Coche(
                id = id,
                matricula = matricula,
                modelo = modelo,
                color = color
            )
            MyLog.d("Actualizando coche: $cocheEditado")
            cocheRepository.sobrescribir(cocheEditado)
            obtenerTodos() // Refresca la lista
        }
    }

    fun filtrar(criterio: String) {
        MyLog.d("viewModel.filtrar por color: $criterio")
        viewModelScope.launch {
            if (criterio.isBlank()) {
                obtenerTodos()
            } else {
                // Creamos el objeto indicando que filtre únicamente por color
                val cocheFiltro = Coche(
                    matricula = "",
                    modelo = "",
                    color = criterio.trim()
                )
                listaCoches = cocheRepository.buscarListaCoche(cocheFiltro)
            }
        }
    }
}



