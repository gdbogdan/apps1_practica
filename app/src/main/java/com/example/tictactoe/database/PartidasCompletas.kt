package com.example.tictactoe.database

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

@Composable
fun PartidasCompletas(
    navController: NavController,
    partidasViewModel: PartidasViewModel,
    partidaId: Int
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PartidasCompletasPortrait(navController, partidasViewModel, partidaId)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            PartidasCompletasLandscape(navController, partidasViewModel, partidaId)
        }
        else -> {
            PartidasCompletasPortrait(navController, partidasViewModel, partidaId)
        }
    }
}