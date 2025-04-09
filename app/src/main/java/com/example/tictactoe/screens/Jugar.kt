package com.example.tictactoe.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

//Posibles posiciones
enum class Simbolo {
    X, O, Vacio
}

@Composable
fun Jugar(navController: NavController, dificultad: Boolean, temporizador: Boolean) {
    var tablero by rememberSaveable { mutableStateOf(Array(3) { Array(3) { Simbolo.Vacio } }) }
    var turno by rememberSaveable { mutableStateOf(Simbolo.X) }
    var ganador by rememberSaveable { mutableStateOf<Simbolo?>(null) }
    val segundos = rememberSaveable { mutableIntStateOf(0) }
    val minutos = rememberSaveable { mutableIntStateOf(2) }
    var juegoTerminado by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
        ) {
            if (temporizador) MostrarTiempo(segundos, minutos, juegoTerminado)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Tablero(tablero) { fila, columna ->
                if (ganador == null && tablero[fila][columna] == Simbolo.Vacio && !juegoTerminado) {
                    val nuevoTablero = tablero.copyOf().map { it.copyOf() }.toTypedArray()
                    nuevoTablero[fila][columna] = turno
                    tablero = nuevoTablero
                    ganador = comprobarGanador(nuevoTablero)
                    turno = if (turno == Simbolo.X) Simbolo.O else Simbolo.X

                    if (turno == Simbolo.O && ganador == null && !juegoTerminado) {
                        val movimientoIA = realizarMovimientoIA(nuevoTablero, dificultad)
                        movimientoIA?.let { (filaIA, columnaIA) ->
                            val nuevoTableroIA = nuevoTablero.copyOf().map { it.copyOf() }.toTypedArray()
                            nuevoTableroIA[filaIA][columnaIA] = Simbolo.O
                            tablero = nuevoTableroIA
                            ganador = comprobarGanador(nuevoTableroIA)
                            turno = Simbolo.X
                        }
                    }

                    if (ganador != null) {
                        juegoTerminado = true
                    }
                }
            }
        }
    }
}

@Composable
fun Tablero(tablero: Array<Array<Simbolo>>, onCasillaClick: (Int, Int) -> Unit) {
    Column {
        for (fila in tablero.indices) {
            Row {
                for (columna in tablero[fila].indices) {
                    Casilla(tablero[fila][columna]) { onCasillaClick(fila, columna) }
                }
            }
        }
    }
}

@Composable
fun Casilla(simbolo: Simbolo, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp) // Tamaño fijo de 50dp
            .border(1.dp, Color.Black)
            .clickable { if (simbolo == Simbolo.Vacio) onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (simbolo) {
                Simbolo.X -> "X"
                Simbolo.O -> "O"
                Simbolo.Vacio -> ""
            }
        )
    }
}

//Compruebo las casillas vacías:
fun comprobarGanador(tablero: Array<Array<Simbolo>>): Simbolo? {
    // Comprobar filas
    for (fila in tablero) {
        if (fila[0] != Simbolo.Vacio && fila[0] == fila[1] && fila[1] == fila[2]) {
            return fila[0]
        }
    }

    // Comprobar columnas
    for (columna in tablero[0].indices) {
        if (tablero[0][columna] != Simbolo.Vacio &&
            tablero[0][columna] == tablero[1][columna] &&
            tablero[1][columna] == tablero[2][columna]
        ) {
            return tablero[0][columna]
        }
    }

    // Comprobar diagonales
    if (tablero[0][0] != Simbolo.Vacio &&
        tablero[0][0] == tablero[1][1] &&
        tablero[1][1] == tablero[2][2]
    ) {
        return tablero[0][0]
    }

    if (tablero[0][2] != Simbolo.Vacio &&
        tablero[0][2] == tablero[1][1] &&
        tablero[1][1] == tablero[2][0]
    ) {
        return tablero[0][2]
    }

    // Comprobar si hay empate
    if (tablero.all { fila -> fila.all { it != Simbolo.Vacio } }) {
        return Simbolo.Vacio // Empate
    }

    return null // No hay ganador aún
}

fun realizarMovimientoIA(tablero: Array<Array<Simbolo>>, dificultad: Boolean): Pair<Int, Int>? {
    val casillasVacias = mutableListOf<Pair<Int, Int>>()
    for (fila in tablero.indices) {
        for (columna in tablero[fila].indices) {
            if (tablero[fila][columna] == Simbolo.Vacio) {
                casillasVacias.add(Pair(fila, columna))
            }
        }
    }

    if (casillasVacias.isEmpty()) return null

    return if (dificultad) {
        casillasVacias.random()
    } else {
        casillasVacias.first()
    }
}

//Funciona para el temporizador

@SuppressLint("DefaultLocale")
@Composable
fun MostrarTiempo(
    segundos: MutableState<Int>,
    minutos: MutableState<Int>,
    juegoTerminado: Boolean
) {
    LaunchedEffect(juegoTerminado) {
        var totalSegundos = minutos.value * 60 + segundos.value

        while (totalSegundos >= 0 && !juegoTerminado) {
            val displayMinutos = totalSegundos / 60
            val displaySegundos = totalSegundos % 60
            minutos.value = displayMinutos
            segundos.value = displaySegundos

            delay(1000)
            totalSegundos--
        }
    }

    Text(String.format("%02d:%02d", minutos.value, segundos.value))
}