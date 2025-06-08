package com.example.tictactoe.database

import kotlinx.coroutines.flow.Flow

class PartidasRepository(private val partidaDao: PartidasDAO) {

    fun getAllPartidas(): Flow<List<Partida>> {
        return partidaDao.getAllPartidas()
    }

    suspend fun insertPartida(partida: Partida) {
        partidaDao.insertPartida(partida)
    }


    fun getPartidaById(partidaId: Int): Flow<Partida?> {
        return partidaDao.getPartidaById(partidaId)
    }
}