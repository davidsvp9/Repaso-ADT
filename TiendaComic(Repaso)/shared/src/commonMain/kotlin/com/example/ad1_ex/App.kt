package com.example.ad1_ex

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.ad1_ex.coche.CocheViewModel
import com.example.ad1_ex.ui.VentanaVer
import com.example.ad1_ex.MyLog
import com.example.ad1_ex.comic.ComicViewModel

@Composable
fun App() {
    MaterialTheme {
        MyLog.d("Plataforma: " + getPlatform().toString())

        val comicViewModel: ComicViewModel = viewModel { ComicViewModel() }

        // Inicializa el contenedor de Voyager con la pantalla inicial
        Navigator(screen = VentanaVer(viewModel = comicViewModel))
    }
}