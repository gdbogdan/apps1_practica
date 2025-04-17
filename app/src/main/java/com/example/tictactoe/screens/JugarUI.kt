package com.example.tictactoe.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.R
import androidx.compose.runtime.State

@Composable
fun JugarUI(
    tablero: Array<Array<Simbolo>>,
    temporizadorActivo: Boolean,
    segundos: Int,
    minutos: Int,
    turno: Simbolo,
    ganador: Simbolo?,
    juegoTerminado: Boolean,
    onCasillaClick: (Int, Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when {
                    ganador != null -> when (ganador) {
                        Simbolo.X -> stringResource(R.string.has_ganado)
                        Simbolo.O -> stringResource(R.string.has_perdido)
                        Simbolo.Vacio -> stringResource(R.string.empate)
                    }
                    juegoTerminado -> stringResource(R.string.fin_del_juego)
                    else -> stringResource(R.string.turno_de, turno.name) //
                },
                style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 56.dp)
        ) {
            if (temporizadorActivo) {
                MostrarTiempo(segundos, minutos)
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Tablero(tablero = tablero) { fila, columna ->
                if (!juegoTerminado) {
                    onCasillaClick(fila, columna)
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun MostrarTiempo(
    segundos:Int,
    minutos: Int
) {
    Text(String.format("%02d:%02d", minutos, segundos))
}