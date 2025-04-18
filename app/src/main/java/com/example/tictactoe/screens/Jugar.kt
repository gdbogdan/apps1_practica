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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.tictactoe.view_models.JugarViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Jugar(
    navController: NavController,
    dificultad: Boolean,
    temporizadorActivo: Boolean,
    minutos: Int,
    segundos: Int,
    viewModel: JugarViewModel = viewModel()
) {
    val tablero by viewModel.tablero
    val mostrarDialogo by viewModel.mostrarDialogoGanador
    val mensajeGanador by viewModel.mensajeGanador
    val turno by viewModel.turno
    val ganador by viewModel.ganador
    val juegoTerminado by viewModel.juegoTerminado

    val tiempoTranscurridoSegundos = rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var temporizadorJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(temporizadorActivo) {
        temporizadorJob?.cancel()
        if (temporizadorActivo) {
            val tiempoLimite = minutos * 60 + segundos
            temporizadorJob = scope.launch {
                while (!juegoTerminado && tiempoTranscurridoSegundos.value < tiempoLimite) {
                    delay(1000)
                    tiempoTranscurridoSegundos.value++
                }
                if (!juegoTerminado && tiempoTranscurridoSegundos.value >= tiempoLimite && tiempoLimite > 0) {
                    viewModel.finalizarJuegoPorTiempoAgotado()
                }
            }
        } else {
            tiempoTranscurridoSegundos.value = 0 // Resetear el tiempo si el temporizador no está activo
        }
    }

    if (mostrarDialogo) {
        AlertDialogGanador(
            mensaje = mensajeGanador,
            onAceptar = {
                viewModel.reiniciarJuego()
                tiempoTranscurridoSegundos.value = 0
            },
            onNavegarResultados = {
                navController.navigate("Resultados") // Pasa la función de navegación aquí
            }
        )
    }

    if (!mostrarDialogo) {
        JugarUI(
            tablero = tablero,
            turno = turno,
            ganador = ganador,
            juegoTerminado = juegoTerminado,
            tiempoTranscurridoSegundos = tiempoTranscurridoSegundos.value,
            minutosLimite = minutos,
            segundosLimite = segundos,
            temporizadorActivo = temporizadorActivo,
            onCasillaClick = { fila, columna ->
                viewModel.jugarCasilla(fila, columna, dificultad)
            }
        )
    }
}

@Composable
fun AlertDialogGanador(mensaje: String, onAceptar: () -> Unit, onNavegarResultados: () -> Unit) {
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
                Button(
                    onClick = {
                        onAceptar()
                        onNavegarResultados()
                    }
                ) {
                    Text(stringResource(R.string.continuar))
                }
            }
        }
    )
}