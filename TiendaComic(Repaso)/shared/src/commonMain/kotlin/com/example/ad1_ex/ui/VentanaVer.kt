package com.example.ad1_ex.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ad1_ex.coche.CocheViewModel
import com.example.ad1_ex.components.DefaultColumn
import com.example.ad1_ex.ui.components.Header1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.ad1_ex.coche.Coche
import com.example.ad1_ex.ui.components.Header2
import com.example.ad1_ex.MyLog
import com.example.ad1_ex.comic.Comic
import com.example.ad1_ex.comic.ComicViewModel

class VentanaVer(
    @Transient  private val viewModel: ComicViewModel
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Por primera vez al cargar la ventana consulta todos los datos
        LaunchedEffect(Unit) {
            viewModel.obtenerTodos()
        }

        val listaComics = viewModel.listaComics

        DefaultColumn {
            Header1("VentanaVer")
            Header2("Controles")


            Button(
                onClick = {
                    MyLog.d("Navegar a ventana insertar")
                    navigator.push(VentanaInsertar(viewModel))
                }
            ) {
                Text("Insertar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Header2("Listado comics")
            // Muestra la lista de coches
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listaComics) { comic ->
                    ComicCard(comic = comic,
                        onDelete={ id -> viewModel.eliminarComic(id) })
                }
            }
        }
    }
}

@Composable
fun ComicCard(
    comic: Comic,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = comic.titulo,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio: ${comic.precio} €  |  Stock: ${comic.stock} uds.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = { onDelete(comic.id) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Borrar")
            }
        }
    }
}