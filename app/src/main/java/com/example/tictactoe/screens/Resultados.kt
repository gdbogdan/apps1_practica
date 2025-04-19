package com.example.tictactoe.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.R
import com.example.tictactoe.view_models.PerfilViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("ContextCastToActivity")
@Composable
fun Resultados(
    navController: NavController,
    perfilViewModel: PerfilViewModel
){
    val madridZoneId = ZoneId.of("Europe/Madrid")
    val fechaHoraActual = remember { LocalDateTime.now(madridZoneId) }
    val formatoFechaHora = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
    val fechaHoraFormateada = fechaHoraActual.format(formatoFechaHora)

    val alias by perfilViewModel.alias
    val dificultad by perfilViewModel.dificultad
    val temporizador by perfilViewModel.temporizador
    val minutosConfigurados by perfilViewModel.minutos
    val segundosConfigurados by perfilViewModel.segundos
    val minutosRestantes by perfilViewModel.minutosRestantes
    val segundosRestantes by perfilViewModel.segundosRestantes

    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        //Fecha y hora:
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = fechaHoraFormateada)
        }

        //Información de la partida:
        Text(text = stringResource(R.string.alias_r, alias))
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
        if (temporizador) {
            Text(
                text = stringResource(
                    R.string.tiempo_introducido_r,
                    minutosConfigurados,
                    segundosConfigurados
                )
            )
            Text(
                text = stringResource(
                    R.string.tiempo_restante_r,
                    minutosRestantes,
                    segundosRestantes
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Campo para introducir el email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                navController.navigate("Email")
            }
        ){
            Text(text = stringResource(R.string.enviar_email))
        }

        Button(
            onClick = {
                navController.navigate("Juego")
            }
        ){
            Text(text = stringResource(R.string.nueva_partida))
        }

        val activity = (LocalContext.current as? Activity)
        Button(
            onClick = { activity?.finish() }
        ) {
            Text(text = stringResource(R.string.salir))
        }
    }
}