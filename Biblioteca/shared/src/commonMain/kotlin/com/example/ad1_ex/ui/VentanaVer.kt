package com.example.ad1_ex.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ad1_ex.components.DefaultColumn
import com.example.ad1_ex.ui.components.Header1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.ad1_ex.ui.components.Header2
import com.example.ad1_ex.MyLog
import com.example.ad1_ex.libro.Libro
import com.example.ad1_ex.libro.LibroViewModel

class VentanaVer(
    private val viewModel: LibroViewModel
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Estado para almacenar el texto del filtro
        var textoFiltroAutor by remember { mutableStateOf("") }

        // Por primera vez al cargar la ventana consulta todos los datos
        LaunchedEffect(Unit) {
            viewModel.obtenerTodos()
        }

        val listaLibros = viewModel.listaLibros

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

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de texto y Botón de filtrado juntos en una fila o columna ordenada
            OutlinedTextField(
                value = textoFiltroAutor,
                onValueChange = { textoFiltroAutor = it },
                label = { Text("Filtrar por autor") },
                placeholder = { Text("Ej: JK Rowlin...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        MyLog.d("Filtrando en Firestore por: $textoFiltroAutor")
                        viewModel.filtrar(textoFiltroAutor)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Filtrar")
                }

                OutlinedButton(
                    onClick = {
                        textoFiltroAutor = ""
                        viewModel.obtenerTodos() // Limpiar filtro y cargar todos
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar / Todos")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Header2("Listado libros")

            // Muestra la lista obtenida de la consulta de Firestore
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listaLibros) { libro ->
                    LibroCard(
                        libro = libro,
                        onEdit = {
                            MyLog.d("Navegar a ventana editar para el libro: ${libro.titulo}")
                            navigator.push(VentanaEditar(viewModel, libro))
                        },
                        onDelete = {
                            MyLog.d("Eliminar libro: ${libro.titulo}")
                            viewModel.eliminar(libro.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LibroCard(
    libro: Libro,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${libro.titulo} ${libro.autor}",
                style = MaterialTheme.typography.titleMedium
            )
            if (libro.isbn.isNotBlank()) {
                Text(
                    text = "ISBN: ${libro.isbn}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Paginas: ${libro.paginas}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Precio: ${libro.precio}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (libro.disponible==true){
                    Text(
                        text = "Disponible: SI",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }else{
                    Text(
                        text = "Disponible: NO",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Text("Editar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}