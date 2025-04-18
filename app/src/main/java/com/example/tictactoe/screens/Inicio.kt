package com.example.tictactoe.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val scrollstate = rememberScrollState()
    val context = LocalContext.current
    val msgPrimerJuego = stringResource(R.string.primerJuego)

    Column(
        Modifier.fillMaxSize().verticalScroll(scrollstate),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.tic_tac_toe),
            contentDescription = stringResource(R.string.letrero)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if(primerJuego){
                Toast.makeText(context, msgPrimerJuego, Toast.LENGTH_LONG).show()
                navController.navigate("Perfil")
            }else{
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
}