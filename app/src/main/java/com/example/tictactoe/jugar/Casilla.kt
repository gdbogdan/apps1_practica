package com.example.tictactoe.jugar

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Casilla(
    simbolo: Simbolo,
    fila: Int,
    columna: Int,
    onCasillaClick: (fila: Int, columna: Int) -> Unit,
    cellSize: Dp
) {
    Box(
        modifier = Modifier
            .size(cellSize)
            .border(1.dp, Color.Black)
            .clickable { onCasillaClick(fila, columna) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (simbolo) {
                Simbolo.X -> "X"
                Simbolo.O -> "O"
                Simbolo.Vacio -> ""
            },
            style = TextStyle(fontSize = (cellSize.value * 0.6f).sp)
        )
    }
}