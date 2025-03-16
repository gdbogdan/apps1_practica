package com.example.tictactoe.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R

@Composable
fun Instrucciones(navController: NavController){
    val scrollState = rememberScrollState()
    Column (
        modifier = Modifier.fillMaxSize()
            .verticalScroll(scrollState)
    ){
        Row (modifier = Modifier.padding(16.dp)){
            Text(
                stringResource(R.string.instrucciones),
                style = TextStyle(textAlign = TextAlign.Justify)
            )
        }
        Row (modifier = Modifier.padding(16.dp)){
            Text(
                stringResource(R.string.instrucciones_dificultad),
                style = TextStyle(textAlign = TextAlign.Justify)
            )
        }
        Row (modifier = Modifier.padding(16.dp)){
            Text(
                stringResource(R.string.instrucciones_perfil),
                style = TextStyle(textAlign = TextAlign.Justify)
            )
        }
        Row (modifier = Modifier.padding(16.dp)
            .align(Alignment.CenterHorizontally)
        ){
            Button(onClick = {navController.navigate("Inicio")}) {
                Text(
                    text = "VOLVER AL INICIO"
                )
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun InstruccionesPreview(){
    Column (
        modifier = Modifier.fillMaxSize()
    ){
        Row {
            Text(
                stringResource(R.string.instrucciones)
            )
        }
        Row {
            Button(onClick = {/*TODO*/}) {
                Text(
                    text = "VOLVER AL INICIO"
                )
            }
        }
    }
}