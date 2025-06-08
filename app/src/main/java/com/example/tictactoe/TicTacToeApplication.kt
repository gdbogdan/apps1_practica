package com.example.tictactoe

import android.app.Application
import com.example.tictactoe.database.PartidasDatabase
import com.example.tictactoe.database.PartidasRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TicTacToeApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { PartidasDatabase.getDatabase(this, applicationScope) }

    val repository by lazy { PartidasRepository(database.partidaDao()) }
}