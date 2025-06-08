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
import androidx.compose.ui.unit.dp

@Composable
fun Casilla(
    simbolo: Simbolo,
    fila: Int,
    columna: Int,
    onCasillaClick: (fila: Int, columna: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .border(1.dp, Color.Black)
            .clickable { onCasillaClick(fila, columna) },
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