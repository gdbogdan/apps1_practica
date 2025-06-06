package com.example.tictactoe.resultados

import com.example.tictactoe.screens.Simbolo
import com.example.tictactoe.screens.TipoVictoria

data class ResultadoDetallado(
    val simbolo: Simbolo,
    val tipo: TipoVictoria?
)