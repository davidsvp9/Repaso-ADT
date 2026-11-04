package com.example.ad1_ex.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.ad1_ex.coche.CocheViewModel
import com.example.ad1_ex.components.DefaultColumn
import com.example.ad1_ex.components.DefaultOutlinedTextField
import com.example.ad1_ex.ui.components.Header1
import com.example.ad1_ex.MyLog
import com.example.ad1_ex.coche.Coche

class VentanaEditar(
    private val viewModel: CocheViewModel,
    private val cocheAEditar: Coche
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Al iniciar la ventana, cargamos los datos del coche en el ViewModel
        LaunchedEffect(Unit) {
            viewModel.cargarCocheParaEditar(cocheAEditar)
        }

        DefaultColumn {
            Header1("VentanaEditar")

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Matrícula
            DefaultOutlinedTextField(
                placeholder = "Matricula",
                texto = viewModel.matricula,
                onTextoChange = { viewModel.matriculaChanged(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Modelo
            DefaultOutlinedTextField(
                placeholder = "Modelo",
                texto = viewModel.modelo,
                onTextoChange = { viewModel.modeloChanged(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Campo Color
            DefaultOutlinedTextField(
                placeholder = "Color",
                texto = viewModel.color,
                onTextoChange = { viewModel.colorChanged(it) }
            )
            Spacer(modifier = Modifier.height(24.dp))

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
                        MyLog.d("Se va a actualizar el coche")
                        viewModel.actualizarCoche()
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