package com.example.tictactoe.inicio

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tictactoe.perfil.PerfilViewModel
import androidx.compose.runtime.getValue

@SuppressLint("ContextCastToActivity")
@Composable
fun Inicio(
    navController: NavController,
    perfilViewModel: PerfilViewModel
){
    val primerJuego by perfilViewModel.primerJuego.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            InicioPortrait(navController, primerJuego)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            InicioLandscape(navController, primerJuego)
        }
        else -> {
            InicioPortrait(navController, primerJuego)
        }
    }
}


