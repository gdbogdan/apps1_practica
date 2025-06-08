package com.example.tictactoe.inicio

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

@SuppressLint("ContextCastToActivity")
@Composable
fun BotonesInicio(
    navController: NavController,
    primerJuego: Boolean
) {
    val context = LocalContext.current
    val msgPrimerJuego = stringResource(R.string.primerJuego)

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