package com.example.tictactoe.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidasDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartida(partida: Partida)

    @Query("SELECT * FROM partidas ORDER BY id DESC")
    fun getAllPartidas(): Flow<List<Partida>>

    @Query("SELECT * FROM partidas WHERE id = :partidaId")
    fun getPartidaById(partidaId: Int): Flow<Partida?>

    @Query("DELETE FROM partidas")
    suspend fun deleteAll()
}