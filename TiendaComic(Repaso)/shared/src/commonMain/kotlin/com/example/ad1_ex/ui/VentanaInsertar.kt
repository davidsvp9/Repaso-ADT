package com.example.ad1_ex.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ad1_ex.comic.ComicViewModel
import com.example.ad1_ex.components.DefaultColumn
import com.example.ad1_ex.components.DefaultOutlinedTextField
import com.example.ad1_ex.ui.components.Header1
import com.example.ad1_ex.MyLog

class VentanaInsertar(
    @Transient private val viewModel: ComicViewModel
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        DefaultColumn {
            Header1("Insertar Nuevo Cómic")

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Título
            DefaultOutlinedTextField(
                placeholder = "Título del cómic",
                texto = viewModel.titulo,
                onTextoChange = { viewModel.tituloChanged(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Precio
            DefaultOutlinedTextField(
                placeholder = "Precio (€)",
                texto = viewModel.precio,
                onTextoChange = { viewModel.precioChanged(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Stock
            DefaultOutlinedTextField(
                placeholder = "Stock (Unidades)",
                texto = viewModel.stock,
                onTextoChange = { viewModel.stockChanged(it) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Atrás
                OutlinedButton(
                    onClick = { navigator.pop() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Atrás")
                }

                // Botón Guardar / Insertar
                Button(
                    onClick = {
                        MyLog.d("Se va a insertar cómic")
                        viewModel.insertarComic()
                        navigator.pop() // Vuelve a la ventana principal
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar Cómic")
                }
            }
        }
    }
}