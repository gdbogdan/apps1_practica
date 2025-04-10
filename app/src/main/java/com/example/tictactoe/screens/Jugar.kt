package com.example.tictactoe.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tictactoe.R
import com.example.tictactoe.view_models.JuegoViewModel
import kotlinx.coroutines.delay

//Posibles posiciones
enum class Simbolo {
    X, O, Vacio
}

@Composable
fun Jugar(navController: NavController, dificultad: Boolean, temporizador: Boolean) {
    val viewModel: JuegoViewModel = viewModel()
    val tablero by viewModel.tablero
    val mostrarDialogo by viewModel.mostrarDialogoGanador
    val mensajeGanador by viewModel.mensajeGanador

    LaunchedEffect(temporizador) {
        if (temporizador) {
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

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
        ) {
            if (temporizador) {
                MostrarTiempo(segundos = viewModel.segundos, minutos = viewModel.minutos)
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Tablero(tablero = tablero) { fila, columna ->
                viewModel.jugarCasilla(fila, columna, dificultad)
            }
        }
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


@Preview(showBackground = true)
@Composable
fun AlertDialogGanadorPreviewGanador() {
    AlertDialogGanador(mensaje = "¡Has perdido!", onAceptar = {})
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


@SuppressLint("DefaultLocale")
@Composable
fun MostrarTiempo(
    segundos: State<Int>,
    minutos: State<Int>
) {
    Text(String.format("%02d:%02d", minutos.value, segundos.value))
}