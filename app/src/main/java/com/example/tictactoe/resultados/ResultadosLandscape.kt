package com.example.tictactoe.resultados

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle // <-- ¡Añadir esta importación!

@SuppressLint("ContextCastToActivity")
@Composable
fun ResultadosLandscape(
    navController: NavController,
    perfilViewModel: PerfilViewModel,
    jugarViewModel: JugarViewModel
) {
    val madridZoneId = ZoneId.of("Europe/Madrid")
    val fechaHoraActual = remember { LocalDateTime.now(madridZoneId) }
    val formatoFechaHora = DateTimeFormatter.ofPattern("'Fecha: ' dd/MM/yyyy ' - Hora: ' HH:mm")
    val fechaHoraFormateada = fechaHoraActual.format(formatoFechaHora)

    // --- MODIFICACIÓN AQUÍ: Observar los StateFlows con collectAsStateWithLifecycle() ---
    val alias by perfilViewModel.alias.collectAsStateWithLifecycle()
    val dificultad by perfilViewModel.dificultad.collectAsStateWithLifecycle()
    val temporizador by perfilViewModel.temporizador.collectAsStateWithLifecycle()
    val minutosConfigurados by perfilViewModel.minutos.collectAsStateWithLifecycle()
    val segundosConfigurados by perfilViewModel.segundos.collectAsStateWithLifecycle()
    val minutosRestantes by perfilViewModel.minutosRestantes.collectAsStateWithLifecycle()
    val segundosRestantes by perfilViewModel.segundosRestantes.collectAsStateWithLifecycle()
    // Asumiendo que casillasRestantes en JugarViewModel es un State<Int> o MutableState<Int>, no un StateFlow<Int>
    val casillasRestantes by jugarViewModel.casillasRestantes
    val email by perfilViewModel.email.collectAsStateWithLifecycle()
    // -------------------------------------------------------------------------------------

    val context = LocalContext.current
    val mensajeVictoria = jugarViewModel.obtenerMensajeVictoriaFormateado(context)

    // --- ¡AQUÍ ESTÁ EL BLOQUE DE CÁLCULO DEL TIEMPO EMPLEADO! ---
    val totalSegundosConfigurados = (minutosConfigurados * 60) + segundosConfigurados
    // Calculamos el tiempo restante en segundos
    val totalSegundosRestantes = (minutosRestantes * 60) + segundosRestantes

    // Calculamos el tiempo empleado en segundos
    // Usamos coerceAtLeast(0) para asegurarnos de que el resultado no sea negativo.
    val tiempoEmpleadoSegundos = (totalSegundosConfigurados - totalSegundosRestantes).coerceAtLeast(0)

    // Convertimos el tiempo empleado en minutos y segundos
    val minutosEmpleados = tiempoEmpleadoSegundos / 60
    val segundosEmpleados = tiempoEmpleadoSegundos % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.resultados),
            style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top
        ) {
            // Columna 1: Datos de la partida
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(0.45f)
            ) {
                Text(text = fechaHoraFormateada, fontSize = 14.sp)
                Text(text = mensajeVictoria, fontSize = 14.sp)
                Text(text = stringResource(R.string.alias_r, alias), fontSize = 14.sp)
                Text(
                    text = stringResource(
                        R.string.casillas_restantes_r, casillasRestantes
                    )
                )
                Text(
                    text = stringResource(
                        R.string.dificultad_r,
                        if (dificultad) stringResource(R.string.dificil) else stringResource(R.string.facil)
                    ),
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(
                        R.string.temporizador_r,
                        if (temporizador) stringResource(R.string.si) else stringResource(R.string.no)
                    ),
                    fontSize = 14.sp
                )

                if (temporizador) {
                    Text(
                        text = stringResource(
                            R.string.tiempo_juego_resumen,
                            // --- ¡CAMBIO AQUÍ! Pasamos los Int directamente, el %02d de strings.xml hará el formato ---
                            minutosEmpleados,
                            segundosEmpleados,
                            minutosConfigurados,
                            segundosConfigurados
                        ),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Columna 2: Email y Botones
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(0.55f) ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {newValue -> perfilViewModel.actualizarEmail(newValue) },
                    label = { Text(stringResource(R.string.email), fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 14.sp) )

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text(
                            stringResource(R.string.enviar_email),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = {
                            perfilViewModel.reiniciarTiempoRestante()
                            navController.navigate("Jugar")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.nueva_partida),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { (context as? Activity)?.finish() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text(stringResource(R.string.salir), textAlign = TextAlign.Center, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}