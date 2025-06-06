package com.example.tictactoe.screens

import android.media.MediaPlayer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.R
import com.example.tictactoe.perfil.PerfilViewModel
import com.example.tictactoe.resultados.ResultadoJuego
import com.example.tictactoe.view_models.JugarViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Jugar(
    navController: NavController,
    perfilViewModel: PerfilViewModel,
    jugarViewModel: JugarViewModel
) {
    val tablero by jugarViewModel.tablero
    val mostrarDialogo by jugarViewModel.mostrarDialogoGanador
    val resultado by jugarViewModel.resultado
    val mensajeGanador = when (resultado) {
        ResultadoJuego.GANASTE -> stringResource(R.string.has_ganado)
        ResultadoJuego.PERDISTE -> stringResource(R.string.has_perdido)
        ResultadoJuego.EMPATE -> stringResource(R.string.empate)
        ResultadoJuego.TIEMPO_AGOTADO -> stringResource(R.string.tiempo_agotado)
        else -> ""
    }
    val turno by jugarViewModel.turno
    val ganador by jugarViewModel.ganador
    val juegoTerminado by jugarViewModel.juegoTerminado

    // Accedemos a los valores del PerfilViewModel
    val dificultad by perfilViewModel.dificultad
    val temporizadorActivo by perfilViewModel.temporizador
    val minutos by perfilViewModel.minutos
    val segundos by perfilViewModel.segundos

    val tiempoTranscurridoSegundos = rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var temporizadorJob by remember { mutableStateOf<Job?>(null) }

    val context = LocalContext.current

    LaunchedEffect(temporizadorActivo) {
        temporizadorJob?.cancel()
        if (temporizadorActivo) {
            val tiempoLimite = minutos * 60 + segundos
            temporizadorJob = scope.launch {
                while (!juegoTerminado && tiempoTranscurridoSegundos.intValue < tiempoLimite) {
                    delay(1000)
                    tiempoTranscurridoSegundos.intValue++
                }
                if (!juegoTerminado && tiempoTranscurridoSegundos.intValue >= tiempoLimite && tiempoLimite > 0) {
                    jugarViewModel.finalizarJuegoPorTiempoAgotado()
                }
            }
        } else {
            tiempoTranscurridoSegundos.intValue = 0
        }
    }

    if (mostrarDialogo || juegoTerminado || (temporizadorActivo && tiempoTranscurridoSegundos.intValue >= (minutos * 60 + segundos) && (minutos * 60 + segundos) > 0)) {
        val tiempoTotalSegundos = minutos * 60 + segundos

        // REPRODUCCIÓN DE SONIDO DIRECTA ANTES DEL ALERTDIALOG
        LaunchedEffect(resultado) {
            //Log.d("SONIDO_DIRECTO", "Mensaje ganador: $mensajeGanador")
            when (mensajeGanador) {
                "¡Has ganado!" -> MediaPlayer.create(context, R.raw.victory)
                "¡Has perdido!" -> MediaPlayer.create(context, R.raw.defeat)
                "¡Tiempo agotado! Has perdido." -> MediaPlayer.create(context, R.raw.defeat)
                "¡Empate!" -> MediaPlayer.create(context, R.raw.tie)
                else -> null
            }?.start()
        }

        AlertDialogGanador(
            mensaje = mensajeGanador,
            onContinuar = {
                //jugarViewModel.reiniciarJuego()
                tiempoTranscurridoSegundos.intValue = 0
                navController.navigate("Resultados")
            },
            perfilViewModel = perfilViewModel,
            tiempoTotalSegundos = tiempoTotalSegundos,
            tiempoTranscurridoSegundos = tiempoTranscurridoSegundos.intValue
        )
    }

    if (!mostrarDialogo) {
        JugarUI(
            tablero = tablero,
            turno = turno,
            ganador = ganador,
            juegoTerminado = juegoTerminado,
            tiempoTranscurridoSegundos = tiempoTranscurridoSegundos.intValue,
            minutosLimite = minutos,
            segundosLimite = segundos,
            temporizadorActivo = temporizadorActivo,
            onCasillaClick = { fila, columna ->
                jugarViewModel.jugarCasilla(fila, columna, dificultad)
            }
        )
    }
}

@Composable
fun AlertDialogGanador(
    mensaje: String,
    onContinuar: () -> Unit,
    perfilViewModel: PerfilViewModel,
    tiempoTotalSegundos: Int,
    tiempoTranscurridoSegundos: Int
) {
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
                        perfilViewModel.calcularTiempoRestante(tiempoTotalSegundos, tiempoTranscurridoSegundos)
                        onContinuar()
                    }
                ) {
                    Text(stringResource(R.string.continuar))
                }
            }
        }
    )
}