package com.example.tictactoe.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.R
import com.example.tictactoe.view_models.PerfilViewModel

@SuppressLint("DefaultLocale")
@Composable
fun Perfil(
    navController: NavController,
    perfilViewModel: PerfilViewModel
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            PerfilPortrait(navController, perfilViewModel)
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            PerfilLandscape(navController, perfilViewModel)
        }
        else -> {
            PerfilPortrait(navController, perfilViewModel)
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PerfilPortrait(
    navController: NavController,
    perfilViewModel: PerfilViewModel
) {
    val context = LocalContext.current
    val toastMsg = stringResource(R.string.toast_config)
    val scrollState = rememberScrollState()

    val isEditing by perfilViewModel.isEditing
    val alias by perfilViewModel.alias
    val dificultad by perfilViewModel.dificultad
    val temporizador by perfilViewModel.temporizador
    val minutos by perfilViewModel.minutos
    val segundos by perfilViewModel.segundos

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Alias
        OutlinedTextField(
            value = alias,
            onValueChange = { if (isEditing) perfilViewModel.actualizarAlias(it) },
            enabled = isEditing,
            placeholder = { Text(stringResource(R.string.placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.alias)) }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Dificultad
        Text(text = stringResource(R.string.dificultad))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            RadioButton(
                selected = !dificultad,
                onClick = { if (isEditing) perfilViewModel.actualizarDificultad(false) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.facil))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = dificultad,
                onClick = { if (isEditing) perfilViewModel.actualizarDificultad(true) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.dificil))
        }

        Spacer(modifier = Modifier.padding(8.dp))

        // Temporizador
        Text(text = stringResource(R.string.temporizador))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            RadioButton(
                selected = !temporizador,
                onClick = { if (isEditing) perfilViewModel.actualizarTemporizador(false) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.no))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = temporizador,
                onClick = { if (isEditing) perfilViewModel.actualizarTemporizador(true) },
                enabled = isEditing
            )
            Text(text = stringResource(R.string.si))
        }

        // Configuración del Tiempo Máximo
        if (temporizador) {
            Text(text = stringResource(R.string.tiempo_maximo))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = String.format("%02d", minutos),
                    onValueChange = { nuevoTexto ->
                        val valor = nuevoTexto.toIntOrNull() ?: 0
                        perfilViewModel.actualizarMinutos(valor)
                    },
                    label = { Text(stringResource(R.string.minutos)) },
                    placeholder = { Text(stringResource(R.string.zero)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = isEditing
                )

                Text(text = ":", modifier = Modifier.padding(horizontal = 8.dp))

                OutlinedTextField(
                    value = String.format("%02d", segundos),
                    onValueChange = { nuevoTexto ->
                        val valor = nuevoTexto.toIntOrNull() ?: 0
                        perfilViewModel.actualizarSegundos(valor)
                    },
                    label = { Text(stringResource(R.string.segundos)) },
                    placeholder = { Text(stringResource(R.string.zero)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = isEditing
                )
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))

        // Botones Editar/Guardar e Inicio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (isEditing) {
                Button(
                    onClick = {
                        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                        perfilViewModel.marcarPrimerJuegoComoJugado()
                        perfilViewModel.actualizarAlias(alias)
                        perfilViewModel.actualizarDificultad(dificultad)
                        perfilViewModel.actualizarTemporizador(temporizador)
                        perfilViewModel.setEditing(false)
                    }
                ) {
                    Text(stringResource(R.string.guardar))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        perfilViewModel.restablecerValoresOriginales()
                    }
                ) {
                    Text(stringResource(R.string.cancelar))
                }
            } else {
                Button(
                    onClick = {
                        perfilViewModel.guardarValoresOriginales(
                            alias,
                            dificultad,
                            temporizador,
                            minutos,
                            segundos
                        )
                        perfilViewModel.setEditing(true)
                    }
                ) {
                    Text(stringResource(R.string.editar))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { navController.navigate("Inicio") }
                ) {
                    Text(stringResource(R.string.inicio))
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PerfilLandscape(
    navController: NavController,
    perfilViewModel: PerfilViewModel
) {
    val context = LocalContext.current

    val isEditing by perfilViewModel.isEditing
    val alias by perfilViewModel.alias
    val dificultad by perfilViewModel.dificultad
    val temporizador by perfilViewModel.temporizador
    val minutos by perfilViewModel.minutos
    val segundos by perfilViewModel.segundos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Fila 1: Alias, Dificultad y Temporizador (INTACTA)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                value = alias,
                onValueChange = { if (isEditing) perfilViewModel.actualizarAlias(it) },
                enabled = isEditing,
                placeholder = { Text(stringResource(R.string.placeholder)) },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.alias)) }
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(0.5f)) {
                Text(text = stringResource(R.string.dificultad))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !dificultad,
                        onClick = { if (isEditing) perfilViewModel.actualizarDificultad(false) },
                        enabled = isEditing
                    )
                    Text(text = stringResource(R.string.facil))
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = dificultad,
                        onClick = { if (isEditing) perfilViewModel.actualizarDificultad(true) },
                        enabled = isEditing
                    )
                    Text(text = stringResource(R.string.dificil))
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(0.6f)) {
                Text(text = stringResource(R.string.temporizador))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !temporizador,
                        onClick = { if (isEditing) perfilViewModel.actualizarTemporizador(false) },
                        enabled = isEditing
                    )
                    Text(text = stringResource(R.string.no))
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = temporizador,
                        onClick = { if (isEditing) perfilViewModel.actualizarTemporizador(true) },
                        enabled = isEditing
                    )
                    Text(stringResource(R.string.si))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Fila 2: Minutos/Segundos (si temporizador) y Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Alineación vertical centrada
        ) {
            // Contenedor para Minutos y Segundos
            if (temporizador) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(0.5f)) {
                    Text(text = stringResource(R.string.tiempo_maximo), modifier = Modifier.padding(end = 8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = String.format("%02d", minutos),
                            onValueChange = { nuevoTexto ->
                                val valor = nuevoTexto.toIntOrNull() ?: 0
                                perfilViewModel.actualizarMinutos(valor)
                            },
                            label = { Text(stringResource(R.string.minutos)) },
                            placeholder = { Text(stringResource(R.string.zero)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            enabled = isEditing
                        )

                        Text(text = ":", modifier = Modifier.padding(horizontal = 8.dp))

                        OutlinedTextField(
                            value = String.format("%02d", segundos),
                            onValueChange = { nuevoTexto ->
                                val valor = nuevoTexto.toIntOrNull() ?: 0
                                perfilViewModel.actualizarSegundos(valor)
                            },
                            label = { Text(stringResource(R.string.segundos)) },
                            placeholder = { Text(stringResource(R.string.zero)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            enabled = isEditing
                        )
                    }
                }
            } else {
                Column(modifier = Modifier.weight(0.5f)) {
                    Spacer(modifier = Modifier.height(0.dp)) // Espacio si no hay temporizador
                }
            }

            // Contenedor para los Botones
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.weight(0.5f)
            ) {
                if (!isEditing) {
                    Button(
                        onClick = {
                            perfilViewModel.guardarValoresOriginales(
                                alias,
                                dificultad,
                                temporizador,
                                minutos,
                                segundos
                            )
                            perfilViewModel.setEditing(true)
                        }
                    ) {
                        Text(stringResource(R.string.editar))
                    }
                    Button(
                        onClick = { navController.navigate("Inicio") }
                    ) {
                        Text(stringResource(R.string.inicio))
                    }
                } else {
                    Button(
                        onClick = {
                            perfilViewModel.marcarPrimerJuegoComoJugado()
                            perfilViewModel.actualizarAlias(alias)
                            perfilViewModel.actualizarDificultad(dificultad)
                            perfilViewModel.actualizarTemporizador(temporizador)
                            perfilViewModel.actualizarMinutos(minutos)
                            perfilViewModel.actualizarSegundos(segundos)
                            perfilViewModel.setEditing(false)
                        }
                    ) {
                        Text(stringResource(R.string.guardar))
                    }
                    Button(
                        onClick = {
                            perfilViewModel.restablecerValoresOriginales()
                        }
                    ) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun PerfilPortraitPreview() {
    val navController = rememberNavController()
    val viewModel: PerfilViewModel = viewModel()
    PerfilPortrait(navController = navController, perfilViewModel = viewModel)
}

@Preview(showBackground = true, device = "spec:parent=pixel_6,orientation=landscape")
@Composable
fun PerfilLandscapePreview() {
    val navController = rememberNavController()
    val viewModel: PerfilViewModel = viewModel()
    PerfilLandscape(navController = navController, perfilViewModel = viewModel)
}