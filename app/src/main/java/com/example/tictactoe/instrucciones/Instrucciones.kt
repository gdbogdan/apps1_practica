package com.example.tictactoe.instrucciones

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

@Composable
fun Instrucciones(navController: NavController) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            InstruccionesPortrait(navController)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            InstruccionesLandscape(navController)
        }
        else -> {
            InstruccionesPortrait(navController)
        }
    }
}