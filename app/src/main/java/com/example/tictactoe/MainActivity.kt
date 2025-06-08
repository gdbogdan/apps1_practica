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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.inicio.Inicio
import com.example.tictactoe.instrucciones.Instrucciones
import com.example.tictactoe.jugar.Jugar
import com.example.tictactoe.partidas.Partidas
import com.example.tictactoe.perfil.Perfil
import com.example.tictactoe.resultados.Resultados
import com.example.tictactoe.perfil.PerfilViewModel
import com.example.tictactoe.view_models.JugarViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedGetBackStackEntry")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val application = application
            val perfilViewModel: PerfilViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
                            @Suppress("UNCHECKED_CAST")
                            return PerfilViewModel(application) as T
                        }
                        throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
            )

            Scaffold(
                topBar = {TopBar(navController)},
                bottomBar = {BottomBar(navController)}
            ) {
                innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable ("Inicio") {
                            Inicio(
                                navController = navController,
                                perfilViewModel = perfilViewModel
                            )
                        }
                        composable ("Instrucciones") {
                            Instrucciones(navController)
                        }
                        composable ("Jugar") {
                            val jugarViewModel: JugarViewModel = viewModel()
                            Jugar(
                                navController = navController,
                                perfilViewModel = perfilViewModel,
                                jugarViewModel = jugarViewModel
                            )
                        }
                        composable ("Perfil"){
                            Perfil(
                                navController = navController,
                                perfilViewModel = perfilViewModel
                            )
                        }
                        composable ( "Partidas"){
                            Partidas(navController)
                        }
                        composable("Resultados") {
                            val jugarViewModel: JugarViewModel = viewModel(
                                navController.getBackStackEntry("Jugar")
                            )

                            Resultados(
                                navController = navController,
                                perfilViewModel = perfilViewModel,
                                jugarViewModel = jugarViewModel
                            )
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
                IconButton(onClick = {navController.navigate("Perfil")}) {
                    Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.Perfil))
                }
            }
        )
    }

    @Composable
    fun BottomBar(navController: NavController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Info, contentDescription = stringResource(R.string.Instrucciones)) },
                label = { Text(stringResource(R.string.Instrucciones)) },
                selected = currentRoute == stringResource(R.string.Instrucciones),
                onClick = { navController.navigate("Instrucciones") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.Inicio)) },
                label = { Text(stringResource(R.string.Inicio))},
                selected = currentRoute == stringResource(R.string.Inicio),
                onClick = { navController.navigate("Inicio") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.Partidas)) },
                label = { Text(stringResource(R.string.Partidas)) },
                selected = currentRoute == stringResource(R.string.Partidas),
                onClick = { navController.navigate("Partidas") }
            )
        }
    }
}