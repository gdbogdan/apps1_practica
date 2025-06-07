package com.example.tictactoe.perfil

import android.annotation.SuppressLint
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
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tictactoe.R

@SuppressLint("DefaultLocale")
@Composable
fun PerfilLandscape(
    navController: NavController,
    perfilViewModel: PerfilViewModel
) {
    val context = LocalContext.current
    val toastMsgConfigurada = stringResource(R.string.toast_config)
    val toastMsgTiempoCero = stringResource(R.string.toast_tiempo_cero)

    // Observar el estado de edición del ViewModel
    val isEditing by perfilViewModel.isEditing

    val aliasDataStore by perfilViewModel.alias.collectAsStateWithLifecycle()
    val dificultadDataStore by perfilViewModel.dificultad.collectAsStateWithLifecycle()
    val temporizadorDataStore by perfilViewModel.temporizador.collectAsStateWithLifecycle()
    val minutosDataStore by perfilViewModel.minutos.collectAsStateWithLifecycle()
    val segundosDataStore by perfilViewModel.segundos.collectAsStateWithLifecycle()

    // Estados locales para la edición temporal, si se cancela, se revierte todo
    var currentAlias by remember { mutableStateOf(aliasDataStore) }
    var currentDificultad by remember { mutableStateOf(dificultadDataStore) }
    var currentTemporizador by remember { mutableStateOf(temporizadorDataStore) }
    var currentMinutos by remember { mutableIntStateOf(minutosDataStore) }
    var currentSegundos by remember { mutableIntStateOf(segundosDataStore) }

    var originalAlias by remember { mutableStateOf(aliasDataStore) }
    var originalDificultad by remember { mutableStateOf(dificultadDataStore) }
    var originalTemporizador by remember { mutableStateOf(temporizadorDataStore) }
    var originalMinutos by remember { mutableIntStateOf(minutosDataStore) }
    var originalSegundos by remember { mutableIntStateOf(segundosDataStore) }

    // Sincronizar estados locales con DataStore cuando isEditing se desactiva
    // o cuando los valores de DataStore cambian (por ejemplo, al guardar o al cargar por primera vez)
    // Usamos LaunchedEffect para reaccionar a cambios en DataStore y actualizar los estados locales.
    // Solo actualizamos los estados locales si NO estamos editando,
    // o si estamos editando y los valores de DataStore cambian (lo cual no debería pasar si estamos editando
    // y no hemos guardado, a menos que otro proceso los cambie).
    // La forma más robusta es reaccionar a `isEditing` y `aliasDataStore`, etc.
    // Cuando `isEditing` es `false`, los estados locales deben reflejar los de DataStore.
    androidx.compose.runtime.LaunchedEffect(isEditing, aliasDataStore, dificultadDataStore, temporizadorDataStore, minutosDataStore, segundosDataStore) {
        if (!isEditing) {
            currentAlias = aliasDataStore
            currentDificultad = dificultadDataStore
            currentTemporizador = temporizadorDataStore
            currentMinutos = minutosDataStore
            currentSegundos = segundosDataStore
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                value = currentAlias,
                onValueChange = { if (isEditing) currentAlias = it },
                enabled = isEditing,
                placeholder = { Text(stringResource(R.string.placeholder)) },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.alias)) }
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(0.5f)) {
                Text(text = stringResource(R.string.dificultad))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !currentDificultad,
                        onClick = { if (isEditing) currentDificultad = false },
                        enabled = isEditing
                    )
                    Text(text = stringResource(R.string.facil))
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = currentDificultad,
                        onClick = { if (isEditing) currentDificultad = true },
                        enabled = isEditing
                    )
                    Text(text = stringResource(R.string.dificil))
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(0.6f)) {
                Text(text = stringResource(R.string.temporizador))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !currentTemporizador,
                        onClick = { if (isEditing) currentTemporizador = false },
                        enabled = isEditing
                    )
                    Text(text = stringResource(R.string.no))
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = currentTemporizador,
                        onClick = { if (isEditing) currentTemporizador = true },
                        enabled = isEditing
                    )
                    Text(stringResource(R.string.si))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contenedor para Minutos y Segundos
            if (currentTemporizador) { // Usar el estado local
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(0.5f)) {
                    Text(text = stringResource(R.string.tiempo_maximo), modifier = Modifier.padding(end = 8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = String.format("%02d", currentMinutos),
                            onValueChange = { nuevoTexto ->
                                if (isEditing) {
                                    val valor = nuevoTexto.toIntOrNull() ?: 0
                                    currentMinutos = valor
                                }
                            },
                            label = { Text(stringResource(R.string.minutos)) },
                            placeholder = { Text(stringResource(R.string.zero)) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            enabled = isEditing
                        )

                        Text(text = ":", modifier = Modifier.padding(horizontal = 8.dp))

                        OutlinedTextField(
                            value = String.format("%02d", currentSegundos),
                            onValueChange = { nuevoTexto ->
                                if (isEditing) {
                                    val valor = nuevoTexto.toIntOrNull() ?: 0
                                    currentSegundos = valor // Actualizar el estado local
                                }
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
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.weight(0.5f)
            ) {
                if (!isEditing) {
                    Button(
                        onClick = {
                            // Al editar, guardar los valores actuales de DataStore como "originales" temporales
                            originalAlias = aliasDataStore
                            originalDificultad = dificultadDataStore
                            originalTemporizador = temporizadorDataStore
                            originalMinutos = minutosDataStore
                            originalSegundos = segundosDataStore

                            // Establecer los estados locales para que reflejen los de DataStore al iniciar la edición
                            currentAlias = aliasDataStore
                            currentDificultad = dificultadDataStore
                            currentTemporizador = temporizadorDataStore
                            currentMinutos = minutosDataStore
                            currentSegundos = segundosDataStore

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
                            if (currentAlias.trim().isEmpty()) { // Usar estado local
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_alias_vacio),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (currentTemporizador && currentMinutos == 0 && currentSegundos == 0) { // Usar estado local
                                Toast.makeText(context, toastMsgTiempoCero, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, toastMsgConfigurada, Toast.LENGTH_SHORT).show()
                                perfilViewModel.marcarPrimerJuegoComoJugado()
                                perfilViewModel.actualizarAlias(currentAlias)
                                perfilViewModel.actualizarDificultad(currentDificultad)
                                perfilViewModel.actualizarTemporizador(currentTemporizador)
                                perfilViewModel.actualizarMinutos(currentMinutos)
                                perfilViewModel.actualizarSegundos(currentSegundos)
                                perfilViewModel.setEditing(false)
                            }
                        }
                    ) {
                        Text(stringResource(R.string.guardar))
                    }
                    Button(
                        onClick = {
                            currentAlias = originalAlias
                            currentDificultad = originalDificultad
                            currentTemporizador = originalTemporizador
                            currentMinutos = originalMinutos
                            currentSegundos = originalSegundos

                            perfilViewModel.setEditing(false)
                        }
                    ) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            }
        }
    }
}