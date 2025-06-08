package com.example.tictactoe.database

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

@SuppressLint("ContextCastToActivity")
@Composable
fun Partidas(
    navController: NavController,
    partidasViewModel: PartidasViewModel
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PartidasResumenPortrait(navController, partidasViewModel)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            PartidasResumenLandscape(navController, partidasViewModel)
        }
        else -> {
            PartidasResumenPortrait(navController, partidasViewModel)
        }
    }
}