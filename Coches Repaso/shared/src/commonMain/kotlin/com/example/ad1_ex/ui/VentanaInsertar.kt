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
import androidx.compose.ui.Alignment
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

class VentanaInsertar(
    private val viewModel: CocheViewModel
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        DefaultColumn {
            Header1("VentanaInsertar")

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

            // Botón de Insertar / Guardar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Separación entre ambos botones
            ) {
                // Botón Atrás (A la izquierda)
                OutlinedButton(
                    onClick = { navigator.pop() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Atrás")
                }

                // Botón Insertar Coche (A la derecha)
                Button(
                    onClick = {
                        MyLog.d("Se va a insertar")
                        viewModel.insertarCoche()
                        navigator.pop() // Vuelve a la pantalla anterior
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Insertar Coche")
                }
            }
        }
    }
}