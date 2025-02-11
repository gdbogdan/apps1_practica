package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.screens.Inicio
import com.example.tictactoe.screens.Instrucciones
import com.example.tictactoe.screens.Jugar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "Inicio", builder = {
                composable ("Inicio") {
                    Inicio(navController)
                }
                composable ("Instrucciones") {
                    Instrucciones(navController)
                }
                composable ("Jugar") {
                    Jugar(navController)
                }
            })
        }
    }
}
