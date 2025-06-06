package com.example.tictactoe.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.R
import androidx.compose.runtime.State

@SuppressLint("DefaultLocale")
@Composable
fun JugarUI(
    tablero: State<List<List<Simbolo>>>,
    turno: Simbolo,
    ganador: Simbolo?,
    juegoTerminado: Boolean,
    onCasillaClick: (Int, Int) -> Unit,
    tiempoTranscurridoSegundos: Int,
    minutosLimite: Int,
    segundosLimite: Int,
    temporizadorActivo: Boolean
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = if (isLandscape) 8.dp else 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isLandscape) Arrangement.Top else Arrangement.Center
        ) {
            Text(
                text = when {
                    ganador != null -> when (ganador) {
                        Simbolo.X -> stringResource(R.string.has_ganado)
                        Simbolo.O -> stringResource(R.string.has_perdido)
                        Simbolo.Vacio -> stringResource(R.string.empate)
                    }
                    juegoTerminado -> stringResource(R.string.fin_del_juego)
                    else -> stringResource(R.string.turno_de, turno.name)
                },
                style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Tablero(tablero = tablero) { fila, columna ->
                if (!juegoTerminado) {
                    onCasillaClick(fila, columna)
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = if (isLandscape) 8.dp else 56.dp)
        ) {
            if (temporizadorActivo) {
                val minutosActuales = tiempoTranscurridoSegundos / 60
                val segundosActuales = tiempoTranscurridoSegundos % 60
                Text(String.format("%02d:%02d / %02d:%02d", minutosActuales, segundosActuales, minutosLimite, segundosLimite))
            }
        }
    }
}