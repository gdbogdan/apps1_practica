package com.example.tictactoe.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

@Composable
fun Inicio(navController: NavController){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.letrero_tic_tac_toe),
            contentDescription = "Letrero Tic Tac Toe"
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espaciado opcional
        Button(onClick = {navController.navigate("Jugar") }) {
            Text(text = "JUGAR")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("Instrucciones") }) {
            Text(text = "INSTRUCCIONES")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "SALIR")
        }
    }
}