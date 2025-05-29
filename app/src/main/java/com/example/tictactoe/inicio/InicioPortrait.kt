package com.example.tictactoe.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

@Composable
fun InicioPortrait(
    navController: NavController,
    primerJuego: Boolean
){
    val context = LocalContext.current
    val msgPrimerJuego = stringResource(R.string.primerJuego)

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImagenInicio()
        Spacer(modifier = Modifier.height(16.dp))
        BotonesInicio(navController, primerJuego, context, msgPrimerJuego)
    }
}
