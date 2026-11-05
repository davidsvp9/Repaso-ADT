package com.example.ad1_ex

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.ad1_ex.ui.VentanaVer
import com.example.ad1_ex.MyLog
import com.example.ad1_ex.libro.LibroViewModel

@Composable
fun App() {
    MaterialTheme {
        MyLog.d("Plataforma: " + getPlatform().toString())

        val libroViewModel: LibroViewModel = viewModel { LibroViewModel() }

        // Inicializa el contenedor de Voyager con la pantalla inicial
        Navigator(screen = VentanaVer(viewModel = libroViewModel))
    }
}