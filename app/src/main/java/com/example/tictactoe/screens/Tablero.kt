package com.example.tictactoe.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
fun Tablero(tablero: Array<Array<Simbolo>>, onCasillaClick: (Int, Int) -> Unit) {
    Column {
        for (fila in tablero.indices) {
            Row {
                for (columna in tablero[fila].indices) {
                    Casilla(simbolo = tablero[fila][columna], onClick = { onCasillaClick(fila, columna) })
                }
            }
        }
    }
}