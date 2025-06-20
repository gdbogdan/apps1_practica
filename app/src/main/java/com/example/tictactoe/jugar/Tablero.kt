package com.example.tictactoe.jugar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp

@Composable
fun Tablero(
    tablero: State<List<List<Simbolo>>>,
    onCasillaClick: (fila: Int, columna: Int) -> Unit,
    cellSize: Dp
) {
    val currentTablero by tablero

    Column {
        currentTablero.forEachIndexed { filaIndex, row ->
            Row {
                row.forEachIndexed { colIndex, simbolo ->
                    Casilla(
                        simbolo = simbolo,
                        fila = filaIndex,
                        columna = colIndex,
                        onCasillaClick = onCasillaClick,
                        cellSize = cellSize
                    )
                }
            }
        }
    }
}