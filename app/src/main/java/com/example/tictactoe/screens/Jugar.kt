package com.example.tictactoe.screens

import android.app.Activity
import android.media.MediaPlayer
import android.widget.Toast
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun Jugar(
    navController: NavController,
    perfilViewModel: PerfilViewModel,
    jugarViewModel: JugarViewModel
) {
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

    val dificultad by perfilViewModel.dificultad.collectAsStateWithLifecycle()
    val temporizadorActivo by perfilViewModel.temporizador.collectAsStateWithLifecycle()
    val minutos by perfilViewModel.minutos.collectAsStateWithLifecycle()
    val segundos by perfilViewModel.segundos.collectAsStateWithLifecycle()

    val tiempoTranscurridoSegundos = rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    var temporizadorJob by remember { mutableStateOf<Job?>(null) }

    val context = LocalContext.current

    LaunchedEffect(temporizadorActivo, minutos, segundos) {
        temporizadorJob?.cancel()

        val tiempoLimite = minutos * 60 + segundos
        if (temporizadorActivo && tiempoLimite > 0) {
            tiempoTranscurridoSegundos.intValue = 0
            temporizadorJob = scope.launch {
                while (!juegoTerminado && tiempoTranscurridoSegundos.intValue < tiempoLimite) {
                    delay(1000)
                    tiempoTranscurridoSegundos.intValue++
                }
                if (!juegoTerminado && tiempoTranscurridoSegundos.intValue >= tiempoLimite) {
                    jugarViewModel.finalizarJuegoPorTiempoAgotado()
                }
            }
        } else {
            tiempoTranscurridoSegundos.intValue = 0
        }
    }

    LaunchedEffect(Unit) {
        jugarViewModel.feedbackMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        jugarViewModel.closeAppEvent.collectLatest {
            (context as? Activity)?.finish()
        }
    }

    if (mostrarDialogo || juegoTerminado || (temporizadorActivo && tiempoTranscurridoSegundos.intValue >= (minutos * 60 + segundos) && (minutos * 60 + segundos) > 0)) {
        val tiempoTotalSegundos = minutos * 60 + segundos

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
            tablero = jugarViewModel.tablero,
            turno = turno,
            ganador = ganador,
            juegoTerminado = juegoTerminado,
            tiempoTranscurridoSegundos = tiempoTranscurridoSegundos.intValue,
            minutosLimite = minutos,
            segundosLimite = segundos,
            temporizadorActivo = temporizadorActivo,
            onCasillaClick = { fila, columna ->
                scope.launch {
                    jugarViewModel.onCasillaClick(fila, columna, context, dificultad)
                }
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