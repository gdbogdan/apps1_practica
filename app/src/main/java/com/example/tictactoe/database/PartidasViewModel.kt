package com.example.tictactoe.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PartidasViewModel(private val repository: PartidasRepository) : ViewModel() {

    val allPartidas: Flow<List<Partida>> = repository.getAllPartidas()


    fun insertPartida(partida: Partida) {
        viewModelScope.launch {
            repository.insertPartida(partida)
        }
    }

    fun getPartidaById(id: Int): Flow<Partida?> {
        return repository.getPartidaById(id)
    }
}

// Clase Factory para crear instancias de PartidasViewModel con el repositorio
class PartidasViewModelFactory(private val repository: PartidasRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartidasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PartidasViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}