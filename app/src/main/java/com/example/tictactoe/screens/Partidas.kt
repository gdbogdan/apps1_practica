package com.example.tictactoe.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.tictactoe.R

@SuppressLint("ContextCastToActivity")
@Composable
fun Partidas(navController: NavController){
    val scrollstate = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollstate),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Estás en la pantalla de Partidas Guardadas")
        Button(
            onClick = {
                navController.navigate("Inicio")
            }
        ) {
            Text(stringResource(R.string.inicio))
        }
    }
}