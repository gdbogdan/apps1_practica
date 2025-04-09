package com.example.tictactoe.view_models

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

    private val _isEditing = mutableStateOf(false)
    val isEditing: State<Boolean> = _isEditing

    private val _originalAlias = mutableStateOf("")
    private val _originalDificultad = mutableStateOf(false)
    private val _originalTemporizador = mutableStateOf(false)

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

    fun setEditing(editing: Boolean) {
        _isEditing.value = editing
    }

    fun guardarValoresOriginales(alias: String, dificultad: Boolean, temporizador: Boolean) {
        _originalAlias.value = alias
        _originalDificultad.value = dificultad
        _originalTemporizador.value = temporizador
    }

    fun restablecerValoresOriginales() {
        _alias.value = _originalAlias.value
        _dificultad.value = _originalDificultad.value
        _temporizador.value = _originalTemporizador.value
        _isEditing.value = false // Salir del modo edición al cancelar
    }
}