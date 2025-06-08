package com.example.tictactoe.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidas")
data class Partida(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val alias: String,
    val fechaHoraFormateada: String,
    val resultado: String,
    val dificultad: Boolean, //True -> Difícil, False ->Fácil
    val temporizador: Boolean, //True -> Sí, False ->No
    val minutosConfigurados: Int,
    val segundosConfigurados: Int,
    val minutosRestantes: Int,
    val segundosRestantes: Int,
    val casillasRestantes: Int
)