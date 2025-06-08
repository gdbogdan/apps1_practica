package com.example.tictactoe.database

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun PartidasCompletasPortrait(
    navController: NavController,
    partidasViewModel: PartidasViewModel,
    partidaId: Int
) {
    val partida by partidasViewModel.getPartidaById(partidaId).collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (partida == null) {
            Text(text = stringResource(R.string.cargando_detalles_partida), style = MaterialTheme.typography.headlineMedium)
        } else {
            PartidaDetalleCard(partida = partida!!)
        }
    }

}

@Composable
fun PartidaDetalleCard(partida: Partida) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Alias y Fecha y Hora (mantienen su posición)
            Text(
                text = stringResource(R.string.alias_etiqueta, partida.alias),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = partida.fechaHoraFormateada,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Dificultad
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(
                    R.string.dificultad_etiqueta,
                    if (partida.dificultad) stringResource(R.string.dificil) else stringResource(R.string.facil)
                ),
                fontSize = 16.sp
            )

            // Temporizador
            Text(
                text = stringResource(
                    R.string.temporizador_etiqueta,
                    if (partida.temporizador) stringResource(R.string.si) else stringResource(R.string.no)
                ),
                fontSize = 16.sp
            )

            if (partida.temporizador) {
                val totalSegundos = partida.minutosConfigurados * 60 + partida.segundosConfigurados
                val restantes = partida.minutosRestantes * 60 + partida.segundosRestantes

                val tiempoEmpleado = when (partida.resultado) {
                    "TIEMPO_AGOTADO" -> totalSegundos // Se agotó todo el tiempo
                    else -> (totalSegundos - restantes).takeIf { restantes > 0 } ?: totalSegundos
                }.coerceAtLeast(0)

                val minutosEmpleados = tiempoEmpleado / 60
                val segundosEmpleados = tiempoEmpleado % 60

                Text(
                    text = stringResource(
                        R.string.tiempo_juego_resumen,
                        minutosEmpleados,
                        segundosEmpleados,
                        partida.minutosConfigurados,
                        partida.segundosConfigurados
                    ),
                    fontSize = 16.sp
                )
            }

            Text(
                text = stringResource(R.string.casillas_restantes_etiqueta, partida.casillasRestantes),
                fontSize = 16.sp
            )


            Spacer(modifier = Modifier.height(10.dp))
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
                }
            )
        }
    }
}