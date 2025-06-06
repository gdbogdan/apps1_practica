package com.example.tictactoe.resultados

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.R
import com.example.tictactoe.perfil.PerfilViewModel
import com.example.tictactoe.view_models.JugarViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("ContextCastToActivity")
@Composable
fun ResultadosPortrait(
    navController: NavController,
    perfilViewModel: PerfilViewModel,
    jugarViewModel: JugarViewModel
) {
    val madridZoneId = ZoneId.of("Europe/Madrid")
    val fechaHoraActual = remember { LocalDateTime.now(madridZoneId) }
    val formatoFechaHora = DateTimeFormatter.ofPattern("'Fecha: ' dd/MM/yyyy ' - Hora: ' HH:mm")
    val fechaHoraFormateada = fechaHoraActual.format(formatoFechaHora)

    val alias by perfilViewModel.alias
    val dificultad by perfilViewModel.dificultad
    val temporizador by perfilViewModel.temporizador
    val minutosConfigurados by perfilViewModel.minutos
    val segundosConfigurados by perfilViewModel.segundos
    val minutosRestantes by perfilViewModel.minutosRestantes
    val segundosRestantes by perfilViewModel.segundosRestantes
    val casillasRestantes by jugarViewModel.casillasRestantes

    val context = LocalContext.current
    val email by perfilViewModel.email
    val mensajeVictoria = jugarViewModel.obtenerMensajeVictoriaFormateado(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.resultados),
            style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(text = fechaHoraFormateada)
        Text(text = mensajeVictoria)
        Text(text = stringResource(R.string.alias_r, alias))
        Text(
            text = stringResource(
                R.string.casillas_restantes_r, casillasRestantes
            )
        )
        Text(
            text = stringResource(
                R.string.dificultad_r,
                if (dificultad) stringResource(R.string.dificil) else stringResource(R.string.facil)
            )
        )
        Text(
            text = stringResource(
                R.string.temporizador_r,
                if (temporizador) stringResource(R.string.si) else stringResource(R.string.no)
            )
        )
        if (temporizador) { //No especifico que el tiempo son en minutos, pues al configurar el Perfil, ya está marcado Minutos y Segundos
            Text(
                text = stringResource(
                    R.string.tiempo_juego_resumen,
                    minutosRestantes,
                    segundosRestantes,
                    minutosConfigurados,
                    segundosConfigurados
                ),
                fontSize = 14.sp
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { newValue -> perfilViewModel.actualizarEmail(newValue) },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                enviarEmail(
                    context = context,
                    fechaHoraFormateada = fechaHoraFormateada,
                    alias = alias,
                    tipoVictoria = mensajeVictoria,
                    dificultad = dificultad,
                    temporizador = temporizador,
                    minutosConfigurados = minutosConfigurados,
                    segundosConfigurados = segundosConfigurados,
                    minutosRestantes = minutosRestantes,
                    segundosRestantes = segundosRestantes,
                    casillasRestantes = casillasRestantes,
                    email = email
                )
            }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.enviar_email)
                )
        }
        Button(
            onClick = {
                perfilViewModel.reiniciarTiempoRestante()
                navController.navigate("Jugar")
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.nueva_partida))
            }
            Button(onClick = { (context as? Activity)?.finish() }, modifier = Modifier.fillMaxWidth()) {
               Text(text = stringResource(R.string.salir))
            }
        }
}