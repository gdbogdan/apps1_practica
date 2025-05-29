package com.example.tictactoe.perfil

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PerfilViewModel : ViewModel() {
    private val _alias = mutableStateOf("")
    val alias: State<String> = _alias

    private val _dificultad = mutableStateOf(false)
    val dificultad: State<Boolean> = _dificultad

    private val _temporizador = mutableStateOf(false)
    val temporizador: State<Boolean> = _temporizador

    // Minutos del temporizador (establecido por el usuario)
    private val _minutos = mutableIntStateOf(0)
    val minutos: State<Int> = _minutos

    // Segundos del temporizador (establecido por el usuario)
    private val _segundos = mutableIntStateOf(0)
    val segundos: State<Int> = _segundos

    // Minutos restantes al finalizar la partida
    private val _minutosRestantes = mutableIntStateOf(0)
    val minutosRestantes: State<Int> = _minutosRestantes

    // Segundos restantes al finalizar la partida
    private val _segundosRestantes = mutableIntStateOf(0)
    val segundosRestantes: State<Int> = _segundosRestantes

    private val _primerJuego = mutableStateOf(true)
    val primerJuego: State<Boolean> = _primerJuego

    private val _isEditing = mutableStateOf(false)
    val isEditing: State<Boolean> = _isEditing

    private val _originalAlias = mutableStateOf("")
    private val _originalDificultad = mutableStateOf(false)
    private val _originalTemporizador = mutableStateOf(false)
    private val _originalMinutos = mutableIntStateOf(0)
    private val _originalSegundos = mutableIntStateOf(0)

    fun actualizarMinutos(nuevosMinutos: Int) {
        _minutos.value = nuevosMinutos
    }

    fun actualizarSegundos(nuevosSegundos: Int) {
        if (nuevosSegundos in 0..59) {
            _segundos.intValue = nuevosSegundos
        }
    }

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

    fun guardarValoresOriginales(alias: String, dificultad: Boolean, temporizador: Boolean, minutos: Int, segundos: Int) {
        _originalAlias.value = alias
        _originalDificultad.value = dificultad
        _originalTemporizador.value = temporizador
        _originalMinutos.intValue = minutos
        _originalSegundos.intValue = segundos
    }

    fun restablecerValoresOriginales() {
        _alias.value = _originalAlias.value
        _dificultad.value = _originalDificultad.value
        _temporizador.value = _originalTemporizador.value
        _minutos.intValue = _originalMinutos.intValue
        _segundos.intValue = _originalSegundos.intValue
        _isEditing.value = false // Salir del modo edición al cancelar
    }

    fun calcularTiempoRestante(tiempoTotalSegundos: Int, tiempoTranscurridoSegundos: Int) {
        val tiempoRestanteSegundos = tiempoTotalSegundos - tiempoTranscurridoSegundos
        _minutosRestantes.value = tiempoRestanteSegundos / 60
        _segundosRestantes.value = tiempoRestanteSegundos % 60
    }

    fun reiniciarTiempoRestante() {
        _minutosRestantes.value = 0
        _segundosRestantes.value = 0
    }
}