package com.example.tictactoe.resultados

import com.example.tictactoe.jugar.Simbolo
import com.example.tictactoe.jugar.TipoVictoria

data class ResultadoDetallado(
    val simbolo: Simbolo,
    val tipo: TipoVictoria?
)