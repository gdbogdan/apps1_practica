package com.example.tictactoe.resultados

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import com.example.tictactoe.perfil.PerfilViewModel
import com.example.tictactoe.view_models.JugarViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun Resultados(
    navController: NavController,
    perfilViewModel: PerfilViewModel,
    jugarViewModel: JugarViewModel
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            ResultadosPortrait(navController, perfilViewModel, jugarViewModel)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            ResultadosLandscape(navController, perfilViewModel, jugarViewModel)
        }
        else -> {
            ResultadosPortrait(navController, perfilViewModel, jugarViewModel)
        }
    }
}