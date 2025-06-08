package com.example.tictactoe.database

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.R
import com.example.tictactoe.ui.theme.ErrorRed
import com.example.tictactoe.ui.theme.SuccessGreen

@Composable
fun PartidasResumenLandscape(
    navController: NavController,
    partidasViewModel: PartidasViewModel
) {
    val allPartidas by partidasViewModel.allPartidas.collectAsState(initial = emptyList())

    if (allPartidas.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sin_partidas_guardadas),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(allPartidas) { partida ->
                PartidaResumenCardLandscape(partida = partida) {
                    navController.navigate("PartidasCompletas/${partida.id}")
                }
            }
        }
    }
}

@Composable
fun PartidaResumenCardLandscape(partida: Partida, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = partida.alias,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = partida.fechaHoraFormateada,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            val resultadoFormateado = partida.resultado.replace("_", " ")
            Text(
                text = resultadoFormateado,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = when (resultadoFormateado) {
                    "GANASTE" -> SuccessGreen
                    "PERDISTE", "TIEMPO AGOTADO" -> ErrorRed
                    "EMPATE" -> Color.Black
                    else -> MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}