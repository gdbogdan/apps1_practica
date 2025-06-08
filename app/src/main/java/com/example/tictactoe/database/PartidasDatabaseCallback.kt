package com.example.tictactoe.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PartidasDatabaseCallback(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        PartidasDatabase.INSTANCE?.let { database ->
            scope.launch(Dispatchers.IO) {
                populateDatabase(database.partidaDao())
            }
        }
    }
}

suspend fun populateDatabase(partidaDao: PartidasDAO) {

    // Inserta datos de ejemplo
    val partida1 = Partida(
        alias = "Rayo",
        fechaHoraFormateada = "07/06/2025 - 10:00",
        resultado = "GANASTE",
        dificultad = false, // Fácil
        temporizador = true, // Con temporizador
        minutosConfigurados = 2,
        segundosConfigurados = 0,
        minutosRestantes = 1,
        segundosRestantes = 45,
        casillasRestantes = 3
    )
    partidaDao.insertPartida(partida1)

    val partida2 = Partida(
        alias = "Tron",
        fechaHoraFormateada = "07/06/2025 - 10:30",
        resultado = "PERDISTE",
        dificultad = true, // Difícil
        temporizador = false, // Sin temporizador
        minutosConfigurados = 0, // No aplica si no hay temporizador, pero se guardan los defaults
        segundosConfigurados = 0,
        minutosRestantes = 0,
        segundosRestantes = 0,
        casillasRestantes = 0 // El juego terminó
    )
    partidaDao.insertPartida(partida2)

    val partida3 = Partida(
        alias = "Veloz",
        fechaHoraFormateada = "07/06/2025 - 11:15",
        resultado = "EMPATE",
        dificultad = false,
        temporizador = true,
        minutosConfigurados = 1,
        segundosConfigurados = 30,
        minutosRestantes = 0,
        segundosRestantes = 0,
        casillasRestantes = 0
    )
    partidaDao.insertPartida(partida3)

    val partida4 = Partida(
        alias = "Lento",
        fechaHoraFormateada = "07/06/2025 - 12:05",
        resultado = "TIEMPO_AGOTADO",
        dificultad = false,
        temporizador = true,
        minutosConfigurados = 0,
        segundosConfigurados = 30,
        minutosRestantes = 0,
        segundosRestantes = 0,
        casillasRestantes = 5 // Quedaron casillas vacías
    )
    partidaDao.insertPartida(partida4)

    println("Base de datos pre-poblada con datos de ejemplo.")
}