package com.example.tictactoe.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            ResultadosPortrait(navController, perfilViewModel)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            ResultadosLandscape(navController, perfilViewModel)
        }
        else -> {
            ResultadosPortrait(navController, perfilViewModel)
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun ResultadosPortrait(
    navController: NavController,
    perfilViewModel: PerfilViewModel
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

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

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
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            val asunto = "Log - $fechaHoraFormateada"
            val cuerpo = buildString {
                appendLine(context.getString(R.string.alias_r, alias))
                appendLine(
                    context.getString(
                        R.string.dificultad_r,
                        if (dificultad) context.getString(R.string.dificil) else context.getString(R.string.facil)
                    )
                )
                appendLine(
                    context.getString(
                        R.string.temporizador_r,
                        if (temporizador) context.getString(R.string.si) else context.getString(R.string.no)
                    )
                )
                if (temporizador) {
                    appendLine(
                        context.getString(
                            R.string.tiempo_introducido_r,
                            minutosConfigurados,
                            segundosConfigurados
                        )
                    )
                    appendLine(
                        context.getString(
                            R.string.tiempo_restante_r,
                            minutosRestantes,
                            segundosRestantes
                        )
                    )
                }
            }

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, asunto)
                putExtra(Intent.EXTRA_TEXT, cuerpo)
            }
            try {
                context.startActivity(Intent.createChooser(intent, "Enviar email..."))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.enviar_email))
        }
        Button(onClick = {
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

@SuppressLint("ContextCastToActivity")
@Composable
fun ResultadosLandscape(
    navController: NavController,
    perfilViewModel: PerfilViewModel
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

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.resultados),
            style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            // Columna 1: Datos de la partida (más ancha)
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(0.3f)
            ) {
                Text(text = fechaHoraFormateada)
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
            }

            Spacer(modifier = Modifier.width(12.dp)) // menos separación entre columnas

            // Columna 2: Email y Botones (más estrecha)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(0.6f)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val asunto = "Log - $fechaHoraFormateada"
                            val cuerpo = buildString {
                                appendLine(context.getString(R.string.alias_r, alias))
                                appendLine(
                                    context.getString(
                                        R.string.dificultad_r,
                                        if (dificultad) context.getString(R.string.dificil) else context.getString(R.string.facil)
                                    )
                                )
                                appendLine(
                                    context.getString(
                                        R.string.temporizador_r,
                                        if (temporizador) context.getString(R.string.si) else context.getString(R.string.no)
                                    )
                                )
                                if (temporizador) {
                                    appendLine(
                                        context.getString(
                                            R.string.tiempo_introducido_r,
                                            minutosConfigurados,
                                            segundosConfigurados
                                        )
                                    )
                                    appendLine(
                                        context.getString(
                                            R.string.tiempo_restante_r,
                                            minutosRestantes,
                                            segundosRestantes
                                        )
                                    )
                                }
                            }

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                                putExtra(Intent.EXTRA_SUBJECT, asunto)
                                putExtra(Intent.EXTRA_TEXT, cuerpo)
                            }
                            try {
                                context.startActivity(Intent.createChooser(intent, "Enviar email..."))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text(stringResource(
                            R.string.enviar_email),
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = {
                            perfilViewModel.reiniciarTiempoRestante()
                            navController.navigate("Jugar")
                        },
                        modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.nueva_partida),
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = { (context as? Activity)?.finish() },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    ) {
                        Text(stringResource(R.string.salir), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
