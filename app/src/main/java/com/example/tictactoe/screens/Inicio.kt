package com.example.tictactoe.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

@SuppressLint("ContextCastToActivity")
@Composable
fun Inicio(
    navController: NavController,
    primerJuego: Boolean
){
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


@SuppressLint("ContextCastToActivity")
@Composable
fun BotonesInicio(
    navController: NavController,
    primerJuego: Boolean,
    context: android.content.Context,
    msgPrimerJuego: String
) {
    Button(onClick = {
        if (primerJuego) {
            Toast.makeText(context, msgPrimerJuego, Toast.LENGTH_LONG).show()
            navController.navigate("Perfil")
        } else {
            navController.navigate("Jugar")
        }
    }) {
        Text(text = stringResource(R.string.jugar))
    }
    Spacer(modifier = Modifier.height(8.dp))
    val activity = (LocalContext.current as? Activity)
    Button(onClick = { activity?.finish() }) {
        Text(text = stringResource(R.string.salir))
    }
}

@Composable
fun InicioLandscape(
    navController: NavController,
    primerJuego: Boolean
){
    val context = LocalContext.current
    val msgPrimerJuego = stringResource(R.string.primerJuego)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ImagenInicio(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                if (primerJuego) {
                    Toast.makeText(context, msgPrimerJuego, Toast.LENGTH_LONG).show()
                    navController.navigate("Perfil")
                } else {
                    navController.navigate("Jugar")
                }
            }) {
                Text(text = stringResource(R.string.jugar))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { (context as? Activity)?.finish() }) {
                Text(text = stringResource(R.string.salir))
            }
        }
    }
}


@Composable
fun ImagenInicio(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.fillMaxWidth(0.6f),
        painter = painterResource(id = R.drawable.tic_tac_toe),
        contentDescription = stringResource(R.string.letrero)
    )
}
