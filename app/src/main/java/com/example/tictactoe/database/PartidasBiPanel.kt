package com.example.tictactoe.database

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tictactoe.R

@Composable
fun PartidasBiPanel(
    partidasViewModel: PartidasViewModel
) {
    val partidas by partidasViewModel.allPartidas.collectAsState(initial = emptyList())
    var partidaSeleccionadaId by remember { mutableStateOf<Int?>(null) }

    // Observar la partida seleccionada por ID
    val partidaSeleccionada by partidasViewModel
        .getPartidaById(partidaSeleccionadaId ?: -1)
        .collectAsState(initial = null)

    // Determinar si estamos en landscape para decidir el layout del BiPanel
    val isLandscapeOrientation = isLandscape()

    // Manejar el caso de que no haya partidas
    if (partidas.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sin_partidas_guardadas)
            )
        }
    } else {
        if (isLandscapeOrientation) {
            Row(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(partidas) { partida ->
                        PartidaResumenCard(partida) {
                            partidaSeleccionadaId = partida.id
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    if (partidaSeleccionada != null) {
                        PartidaDetalleCardLandscape(partidaSeleccionada!!)
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(stringResource(R.string.selecciona_una_partida))
                        }
                    }
                }
            }
        } else { // Portrait
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f) // Ocupa la mitad superior
                        .padding(8.dp)
                ) {
                    items(partidas) { partida ->
                        PartidaResumenCard(partida) {
                            partidaSeleccionadaId = partida.id
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f) // Ocupa la mitad inferior
                        .padding(8.dp)
                ) {
                    if (partidaSeleccionada != null) {
                        PartidaDetalleCard(partidaSeleccionada!!)
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(stringResource(R.string.selecciona_una_partida))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun isLandscape(): Boolean {
    val orientation = LocalConfiguration.current.orientation
    return orientation == Configuration.ORIENTATION_LANDSCAPE
}