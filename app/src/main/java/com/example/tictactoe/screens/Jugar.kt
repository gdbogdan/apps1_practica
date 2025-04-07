package com.example.tictactoe.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay


@Composable
fun Jugar(navController: NavController, dificultad: MutableState<Boolean>, temporizador: MutableState<Boolean>){
    var segundos = rememberSaveable { mutableIntStateOf(0) }
    var minutos = rememberSaveable { mutableIntStateOf(2) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
        ) {
            if(temporizador.value) mostrarTiempo(segundos, minutos)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // El resto de tu contenido irá aquí
        }
    }
}

@Composable
private fun mostrarTiempo(segundos: MutableState<Int>, minutos: MutableState<Int>) {
    LaunchedEffect(true) {
        var totalSegundos = minutos.value * 60 + segundos.value // Convierte a segundos totales
        while (totalSegundos >= 0) {
            val displayMinutos = totalSegundos / 60
            val displaySegundos = totalSegundos % 60
            minutos.value = displayMinutos
            segundos.value = displaySegundos

            delay(1000) // Espera 1 segundo
            totalSegundos--
        }
    }
    Text(String.format("%02d:%02d", minutos.value, segundos.value))
}