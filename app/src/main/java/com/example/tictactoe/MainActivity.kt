package com.example.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.screens.Inicio
import com.example.tictactoe.screens.Instrucciones
import com.example.tictactoe.screens.Jugar

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Scaffold(
                topBar = {TopBar(navController)},
                bottomBar = {BottomBar(navController)}
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "Inicio",
                    builder = {
                        composable ("Inicio") {
                            Inicio(navController)
                        }
                        composable ("Instrucciones") {
                            Instrucciones(navController)
                        }
                        composable ("Jugar") {
                            Jugar(navController)
                        }
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController){
        TopAppBar(
            title = { Text("Tic Tac Toe")},
            actions = {
                IconButton(onClick = {navController.navigate("Inicio")}) {
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
                icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "Jugar") },
                label = { Text("Jugar") },
                selected = false, // Necesitarás lógica para esto
                onClick = { navController.navigate("Jugar") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Menu, contentDescription = "Partidas") },
                label = { Text("Partidas") },
                selected = false, // Necesitarás lógica para esto
                onClick = { /* Navegar a partidas */ }
            )
        }
    }
}