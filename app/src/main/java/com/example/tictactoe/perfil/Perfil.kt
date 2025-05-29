package com.example.tictactoe.perfil

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

@SuppressLint("DefaultLocale")
@Composable
fun Perfil(
    navController: NavController,
    perfilViewModel: PerfilViewModel
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PerfilPortrait(navController, perfilViewModel)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            PerfilLandscape(navController, perfilViewModel)
        }
        else -> {
            PerfilPortrait(navController, perfilViewModel)
        }
    }
}