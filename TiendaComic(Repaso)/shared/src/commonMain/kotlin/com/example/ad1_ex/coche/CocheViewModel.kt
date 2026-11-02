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
}



