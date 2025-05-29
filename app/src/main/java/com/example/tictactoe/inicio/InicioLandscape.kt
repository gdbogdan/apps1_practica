package com.example.tictactoe.inicio

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

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