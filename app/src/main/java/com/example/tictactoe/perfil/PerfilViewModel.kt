package com.example.tictactoe.perfil

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferencesRepository = PerfilPreferencesRepository(application.applicationContext)

    // --- Variables que se persisten con DataStore (todas son StateFlows) ---
    val alias: StateFlow<String> = userPreferencesRepository.userPreferencesFlow
        .map { it.alias }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val dificultad: StateFlow<Boolean> = userPreferencesRepository.userPreferencesFlow
        .map { it.dificultad }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val temporizador: StateFlow<Boolean> = userPreferencesRepository.userPreferencesFlow
        .map { it.temporizador }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val minutos: StateFlow<Int> = userPreferencesRepository.userPreferencesFlow
        .map { it.minutos }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val segundos: StateFlow<Int> = userPreferencesRepository.userPreferencesFlow
        .map { it.segundos }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val primerJuego: StateFlow<Boolean> = userPreferencesRepository.userPreferencesFlow
        .map { it.primerJuego }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val email: StateFlow<String> = userPreferencesRepository.userPreferencesFlow
        .map { it.email }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val minutosRestantes: StateFlow<Int> = userPreferencesRepository.userPreferencesFlow
        .map { it.minutosRestantes }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val segundosRestantes: StateFlow<Int> = userPreferencesRepository.userPreferencesFlow
        .map { it.segundosRestantes }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // --- Variable que NO se persiste (se mantiene como mutableStateOf para el estado de la UI) ---
    private val _isEditing = mutableStateOf(false)
    val isEditing: State<Boolean> = _isEditing


    // --- Funciones de actualización (todas llaman al repositorio para guardar en DataStore) ---
    fun actualizarAlias(nuevoAlias: String) { viewModelScope.launch { userPreferencesRepository.updateAlias(nuevoAlias) } }
    fun actualizarDificultad(nuevaDificultad: Boolean) { viewModelScope.launch { userPreferencesRepository.updateDificultad(nuevaDificultad) } }
    fun actualizarTemporizador(nuevoTemporizador: Boolean) { viewModelScope.launch { userPreferencesRepository.updateTemporizador(nuevoTemporizador) } }
    fun actualizarMinutos(nuevosMinutos: Int) { viewModelScope.launch { userPreferencesRepository.updateMinutos(nuevosMinutos) } }
    fun actualizarSegundos(nuevosSegundos: Int) {
        if (nuevosSegundos in 0..59) {
            viewModelScope.launch { userPreferencesRepository.updateSegundos(nuevosSegundos) }
        }
    }
    fun marcarPrimerJuegoComoJugado() { viewModelScope.launch { userPreferencesRepository.updatePrimerJuego(false) } }

    fun actualizarEmail(nuevoEmail: String) {
        viewModelScope.launch { userPreferencesRepository.updateEmail(nuevoEmail) }
    }

    fun setEditing(editing: Boolean) { _isEditing.value = editing }

    fun calcularTiempoRestante(tiempoTotalSegundos: Int, tiempoTranscurridoSegundos: Int) {
        val tiempoRestanteSegundos = tiempoTotalSegundos - tiempoTranscurridoSegundos
        viewModelScope.launch {
            userPreferencesRepository.updateMinutosRestantes(tiempoRestanteSegundos / 60)
            userPreferencesRepository.updateSegundosRestantes(tiempoRestanteSegundos % 60)
        }
    }

    fun reiniciarTiempoRestante() {
        viewModelScope.launch {
            userPreferencesRepository.updateMinutosRestantes(0)
            userPreferencesRepository.updateSegundosRestantes(0)
        }
    }
}