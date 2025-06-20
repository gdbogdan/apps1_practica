package com.example.tictactoe.database

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

@SuppressLint("ContextCastToActivity") @Composable
fun Partidas(
    navController: NavController,
    partidasViewModel: PartidasViewModel,
    isTablet: Boolean
) {
    if (isTablet) {
        PartidasBiPanel(
            partidasViewModel = partidasViewModel

        )
    } else {
        val configuration = LocalConfiguration.current
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                PartidasResumenPortrait(
                    partidasViewModel = partidasViewModel,
                    navController = navController
                )
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                PartidasResumenLandscape(
                    partidasViewModel = partidasViewModel,
                    navController = navController
                )
            }
            else -> {
                PartidasResumenPortrait(
                    partidasViewModel = partidasViewModel,
                    navController = navController
                )
            }
        }
    }
}