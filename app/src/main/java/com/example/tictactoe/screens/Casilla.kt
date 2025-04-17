package com.example.tictactoe.screens

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