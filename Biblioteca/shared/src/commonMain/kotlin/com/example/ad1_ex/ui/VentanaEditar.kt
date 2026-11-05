package com.example.ad1_ex.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ad1_ex.components.DefaultColumn
import com.example.ad1_ex.components.DefaultOutlinedTextField
import com.example.ad1_ex.ui.components.Header1
import com.example.ad1_ex.MyLog
import com.example.ad1_ex.libro.Libro
import com.example.ad1_ex.libro.LibroViewModel

class VentanaEditar(
    private val viewModel: LibroViewModel,
    private val libroAEditar: Libro
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Al iniciar la ventana, cargamos los datos del coche en el ViewModel
        LaunchedEffect(Unit) {
            viewModel.cargarLibroParaEditar(libroAEditar)
        }

        DefaultColumn {
            Header1("VentanaEditar")

            Spacer(modifier = Modifier.height(16.dp))

            // Campo ISBN
            DefaultOutlinedTextField(
                placeholder = "ISBN",
                texto = viewModel.isbn,
                onTextoChange = { viewModel.isbnChanged(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Titulo
            DefaultOutlinedTextField(
                placeholder = "Titulo",
                texto = viewModel.titulo,
                onTextoChange = { viewModel.tituloChanged(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Autor
            DefaultOutlinedTextField(
                placeholder = "Autor",
                texto = viewModel.autor,
                onTextoChange = { viewModel.autorChanged(it) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Campo Paginas
            DefaultOutlinedTextField(
                placeholder = "Paginas",
                texto = viewModel.paginas,
                onTextoChange = { viewModel.paginasChanged(it) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Campo Precio
            DefaultOutlinedTextField(
                placeholder = "Precio",
                texto = viewModel.precio,
                onTextoChange = { viewModel.precioChanged(it) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = viewModel.disponible,
                    onCheckedChange = { viewModel.disponibleChanged(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Disponible")
            }

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Atrás
                OutlinedButton(
                    onClick = {
                        viewModel.limpiarFormulario()
                        navigator.pop()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Atrás")
                }

                // Botón Guardar Cambios
                Button(
                    onClick = {
                        MyLog.d("Se va a actualizar el libro")
                        viewModel.actualizarLibro()
                        viewModel.limpiarFormulario()
                        navigator.pop() // Vuelve a la pantalla anterior
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}