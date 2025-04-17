package com.example.tictactoe.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tictactoe.R
import com.example.tictactoe.view_models.JuegoViewModel

@Composable
fun Jugar(
    navController: NavController,
    dificultad: Boolean,
    temporizadorActivo: Boolean,
    viewModel: JuegoViewModel = viewModel()
) {
    val tablero by viewModel.tablero
    val mostrarDialogo by viewModel.mostrarDialogoGanador
    val mensajeGanador by viewModel.mensajeGanador
    val turno by viewModel.turno
    val ganador by viewModel.ganador
    val juegoTerminado by viewModel.juegoTerminado
    val segundos by viewModel.segundos
    val minutos by viewModel.minutos

    LaunchedEffect(temporizadorActivo) {
        if (temporizadorActivo) {
            viewModel.iniciarTemporizador()
        } else {
            viewModel.detenerTemporizador()
        }
    }

    if (mostrarDialogo) {
        AlertDialogGanador(
            mensaje = mensajeGanador,
            onAceptar = {
                viewModel.reiniciarJuego()
                navController.navigate("Resultados")
            }
        )
    }

    if (!mostrarDialogo) {
        JugarUI(
            tablero = tablero,
            temporizadorActivo = temporizadorActivo,
            segundos = segundos,
            minutos = minutos,
            turno = turno,
            ganador = ganador,
            juegoTerminado = juegoTerminado,
            onCasillaClick = { fila, columna ->
                viewModel.jugarCasilla(fila, columna, dificultad)
            }
        )
    }
}

@Composable
fun AlertDialogGanador(mensaje: String, onAceptar: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* No hacer nada al tocar fuera */ },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = mensaje,
                    style = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { onAceptar() }) {
                    Text(stringResource(R.string.continuar))
                }
            }
        }
    )
}