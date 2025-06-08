package com.example.tictactoe.partidas

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

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
        Text("PRÓXIMAMENTE DISPONIBLE")
    }
}