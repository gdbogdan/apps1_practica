package com.example.tictactoe.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R
import com.example.tictactoe.view_models.PerfilViewModel

@Composable
fun Configuracion (
    navController: NavController,
    perfilViewModel: PerfilViewModel
){
    val context = LocalContext.current
    val toastMsg = stringResource(R.string.toast_config)
    val scrollState = rememberScrollState()

    val isEditing by perfilViewModel.isEditing
    val alias by perfilViewModel.alias
    val dificultad by perfilViewModel.dificultad
    val temporizador by perfilViewModel.temporizador

    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)){
        // Alias
        Text(text = stringResource(R.string.alias), modifier = Modifier.padding(bottom = 8.dp))
        TextField(
            value = alias,
            onValueChange = { if (isEditing) perfilViewModel.actualizarAlias(it) },
            enabled = isEditing,
            placeholder = { Text(stringResource(R.string.placeholder)) }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Dificultad
        Text(text = stringResource(R.string.dificultad))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            RadioButton(
                selected = !dificultad,
                onClick = { if (isEditing) perfilViewModel.actualizarDificultad(false) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.facil))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = dificultad,
                onClick = { if (isEditing) perfilViewModel.actualizarDificultad(true) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.dificil))
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Temporizador
        Text(text = stringResource(R.string.temporizador))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            RadioButton(
                selected = !temporizador,
                onClick = { if (isEditing) perfilViewModel.actualizarTemporizador(false) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.no))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = temporizador,
                onClick = { if (isEditing) perfilViewModel.actualizarTemporizador(true) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.si))
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Botones Editar/Guardar e Inicio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (isEditing) {
                // Modo Edición
                Button(
                    onClick = {
                        // Guardar los cambios
                        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                        perfilViewModel.marcarPrimerJuegoComoJugado()
                        perfilViewModel.setEditing(false)
                    }
                ) {
                    Text(stringResource(R.string.guardar))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        // Cancelar los cambios
                        perfilViewModel.restablecerValoresOriginales()
                    }
                ) {
                    Text(stringResource(R.string.cancelar))
                }
            } else {
                Button(
                    onClick = {
                        perfilViewModel.guardarValoresOriginales(
                            alias,
                            dificultad,
                            temporizador
                        )
                        perfilViewModel.setEditing(true)
                    }
                ) {
                    Text(stringResource(R.string.editar))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { navController.navigate("Inicio") }
                ) {
                    Text(stringResource(R.string.inicio))
                }
            }
        }
    }
}