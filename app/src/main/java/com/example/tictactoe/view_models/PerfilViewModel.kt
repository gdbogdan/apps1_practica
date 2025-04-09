package com.example.tictactoe.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PerfilViewModel : ViewModel() {
    private val _alias = mutableStateOf("")
    val alias: State<String> = _alias

    private val _dificultad = mutableStateOf(false)
    val dificultad: State<Boolean> = _dificultad

    private val _temporizador = mutableStateOf(false)
    val temporizador: State<Boolean> = _temporizador

    private val _primerJuego = mutableStateOf(true)
    val primerJuego: State<Boolean> = _primerJuego

    fun actualizarAlias(nuevoAlias: String) {
        _alias.value = nuevoAlias
    }

    fun actualizarDificultad(nuevaDificultad: Boolean) {
        _dificultad.value = nuevaDificultad
    }

    fun actualizarTemporizador(nuevoTemporizador: Boolean) {
        _temporizador.value = nuevoTemporizador
    }

    fun marcarPrimerJuegoComoJugado() {
        _primerJuego.value = false
    }
}