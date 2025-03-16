package com.example.tictactoe.screens

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

@SuppressLint("ContextCastToActivity")
@Composable
fun Inicio(navController: NavController){
    val scrollstate = rememberScrollState()
    Column(
        Modifier.fillMaxSize().verticalScroll(scrollstate),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.tic_tac_toe),
            contentDescription = "Letrero Tic Tac Toe"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigate("Jugar") }) {
            Text(text = "JUGAR")
        }
        Spacer(modifier = Modifier.height(8.dp))
        val activity = (LocalContext.current as? Activity)
        Button(onClick = { activity?.finish() }) {
            Text(text = "SALIR")
        }
    }
}