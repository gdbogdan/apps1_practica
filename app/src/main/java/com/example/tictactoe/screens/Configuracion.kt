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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

@Composable
fun Configuracion (
    navController: NavController,
    alias: String,
    dificultad: Boolean,
    temporizador: Boolean,
    primerJuegoEditado: () -> Unit,
    onAliasChange: (String) -> Unit,
    onDificultadChange: (Boolean) -> Unit,
    onTemporizadorChange: (Boolean) -> Unit
){
    val context = LocalContext.current
    val isEditing = rememberSaveable { mutableStateOf(false) }
    val toastMsg = stringResource(R.string.toast_config)
    val scrollState = rememberScrollState()

    //Valores originales
    val originalAlias = rememberSaveable { mutableStateOf(alias) }
    val originalDificultad = rememberSaveable { mutableStateOf(dificultad) }
    val originalTemporizador = rememberSaveable { mutableStateOf(temporizador) }

    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)){
        // Alias
        Text(text = stringResource(R.string.alias), modifier = Modifier.padding(bottom = 8.dp))
        TextField(
            value = alias,
            onValueChange = { if (isEditing.value) onAliasChange(it) },
            enabled = isEditing.value,
            placeholder = { Text(stringResource(R.string.placeholder)) }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Dificultad
        Text(text = stringResource(R.string.dificultad))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            RadioButton(
                selected = !dificultad,
                onClick = { if (isEditing.value) onDificultadChange(false) },
                enabled = isEditing.value
            )
            Text(text = stringResource(R.string.facil))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = dificultad,
                onClick = { if (isEditing.value) onDificultadChange(true) },
                enabled = isEditing.value
            )
            Text(text = stringResource(R.string.dificil))
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Temporizador
        Text(text = stringResource(R.string.temporizador))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            RadioButton(
                selected = !temporizador,
                onClick = { if (isEditing.value) onTemporizadorChange(false) },
                enabled = isEditing.value
            )
            Text(text = stringResource(R.string.no))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = temporizador,
                onClick = { if (isEditing.value) onTemporizadorChange(true) },
                enabled = isEditing.value
            )
            Text(text = stringResource(R.string.si))
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Botones Editar/Guardar e Inicio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (isEditing.value) {
                // Modo Edición
                Button(
                    onClick = {
                        // Guardar los cambios
                        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                        primerJuegoEditado()
                        originalAlias.value = alias
                        originalDificultad.value = dificultad
                        originalTemporizador.value = temporizador
                        isEditing.value = false
                    }
                ) {
                    Text(stringResource(R.string.guardar))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        // Cancelar los cambios
                        onAliasChange(originalAlias.value)
                        onDificultadChange(originalDificultad.value)
                        onTemporizadorChange(originalTemporizador.value)
                        isEditing.value = false
                    }
                ) {
                    Text(stringResource(R.string.cancelar))
                }
            } else {
                Button(
                    onClick = {
                        isEditing.value = true
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