package com.example.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.screens.Configuracion
import com.example.tictactoe.screens.Inicio
import com.example.tictactoe.screens.Instrucciones
import com.example.tictactoe.screens.Jugar
import com.example.tictactoe.screens.Partidas

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var alias = rememberSaveable { mutableStateOf ("") }
            var dificultad = rememberSaveable { mutableStateOf(false) } //false = Nivel fácil y true = Nivel difícil
            var temporizador = rememberSaveable { mutableStateOf(false) } //false = Sin temporizador y true = Con temporizador
            var primerJuego = rememberSaveable { mutableStateOf(true) } //Para saber si es la primera vez que se juega

            val primerJuegoEditado:() -> Unit = {
                primerJuego.value = false
            }

            Scaffold(
                topBar = {TopBar(navController)},
                bottomBar = {BottomBar(navController)}
            ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable ("Inicio") {
                            Inicio(navController, primerJuego)
                        }
                        composable ("Instrucciones") {
                            Instrucciones(navController)
                        }
                        composable ("Jugar") {
                            Jugar(navController)
                        }
                        composable ( "Configuracion"){
                            Configuracion(navController, alias,dificultad,temporizador, primerJuegoEditado)
                        }

                        composable ( "Partidas"){
                            Partidas(navController)
                        }
                    }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController){
        TopAppBar(
            title = { Text("Tic Tac Toe")},
            actions = {
                IconButton(onClick = {navController.navigate("Configuracion")}) {
                    Icon(Icons.Filled.Settings, contentDescription = "Configuracion")
                }
            }
        )
    }

    @Composable
    fun BottomBar(navController: NavController) {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Info, contentDescription = "Instrucciones") },
                label = { Text("Instrucciones") },
                selected = false, // Necesitarás lógica para esto
                onClick = { navController.navigate("Instrucciones") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = false, // Necesitarás lógica para esto
                onClick = { navController.navigate("Inicio") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Menu, contentDescription = "Partidas") },
                label = { Text("Partidas") },
                selected = false, // Necesitarás lógica para esto
                onClick = { navController.navigate("Partidas") }
            )
        }
    }
}