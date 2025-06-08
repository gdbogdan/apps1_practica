package com.example.tictactoe.database

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidasCompletasLandscape(
    navController: NavController,
    partidasViewModel: PartidasViewModel,
    partidaId: Int
) {
    val partida by partidasViewModel.getPartidaById(partidaId).collectAsState(initial = null)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (partida == null) {
            Text(text = stringResource(R.string.cargando_detalles_partida), style = MaterialTheme.typography.headlineMedium)
        } else {
            PartidaDetalleCardLandscape(partida = partida!!)
        }
    }
}

@Composable
fun PartidaDetalleCardLandscape(partida: Partida) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), // Padding ajustado
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.alias_etiqueta, partida.alias),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = partida.fechaHoraFormateada,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.dificultad_etiqueta,
                        if (partida.dificultad) stringResource(R.string.dificil) else stringResource(R.string.facil)
                    ),
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(R.string.casillas_restantes_etiqueta, partida.casillasRestantes),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.temporizador_etiqueta,
                        if (partida.temporizador) stringResource(R.string.si) else stringResource(R.string.no)
                    ),
                    fontSize = 16.sp
                )
                if (partida.temporizador) {
                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Configurado: ${partida.minutosConfigurados} min ${partida.segundosConfigurados} seg",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Restante: ${partida.minutosRestantes} min ${partida.segundosRestantes} seg",
                            fontSize = 14.sp
                        )
                    }
                }
            }
            val resultadoFormateado = partida.resultado.replace("_", " ")
            Text(
                text = resultadoFormateado,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = when (resultadoFormateado) {
                    "GANASTE" -> SuccessGreen
                    "PERDISTE", "TIEMPO AGOTADO" -> ErrorRed
                    "EMPATE" -> Color.Black
                    else -> MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}