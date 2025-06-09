package com.example.tictactoe.jugar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.R
import kotlin.math.min

@SuppressLint("DefaultLocale", "UnusedBoxWithConstraintsScope") // Mantener si sigue siendo necesaria para String.format
@Composable
fun JugarUI(
    tablero: State<List<List<Simbolo>>>,
    turno: Simbolo,
    ganador: Simbolo?,
    juegoTerminado: Boolean,
    onCasillaClick: (Int, Int) -> Unit,
    tiempoTranscurridoSegundos: Int,
    minutosLimite: Int,
    segundosLimite: Int,
    temporizadorActivo: Boolean
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier.fillMaxSize(), // El contenedor principal ocupa toda la pantalla
        horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
    ) {
        // Sección superior para el turno/ganador y el temporizador
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Espacio entre los elementos
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna para el texto de turno/ganador
            Column(
                modifier = Modifier.weight(1f), // Permite que ocupe el espacio disponible
                horizontalAlignment = Alignment.CenterHorizontally // Centra el texto en esta columna
            ) {
                Text(
                    text = when {
                        ganador != null -> when (ganador) {
                            Simbolo.X -> stringResource(R.string.has_ganado)
                            Simbolo.O -> stringResource(R.string.has_perdido)
                            Simbolo.Vacio -> stringResource(R.string.empate)
                        }
                        juegoTerminado -> stringResource(R.string.fin_del_juego)
                        else -> stringResource(R.string.turno_de, turno.name)
                    },
                    style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Temporizador (si está activo)
            if (temporizadorActivo) {
                val minutosActuales = tiempoTranscurridoSegundos / 60
                val segundosActuales = tiempoTranscurridoSegundos % 60
                Text(String.format("%02d:%02d / %02d:%02d", minutosActuales, segundosActuales, minutosLimite, segundosLimite))
            }
        }

        // Sección para el tablero, usando BoxWithConstraints para que ocupe el espacio restante
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth() // Toma todo el ancho disponible del padre (Column)
                .weight(1f) // Esto hace que BoxWithConstraints ocupe TODO el espacio vertical restante
                .padding(horizontal = 16.dp, vertical = 8.dp) // Padding alrededor del área del tablero
        ) {
            // Obtener la densidad local para convertir píxeles a Dp
            val density = LocalDensity.current

            // Convertir las dimensiones máximas disponibles del BoxWithConstraints de Px a Dp
            val availableWidthDp = with(density) { constraints.maxWidth.toDp() }
            val availableHeightDp = with(density) { constraints.maxHeight.toDp() }

            // Calcular el tamaño ideal del tablero (lado del cuadrado) en Dp
            // Usamos .value para obtener el valor Float del Dp para la función min,
            // y luego convertimos el resultado de nuevo a Dp.
            val boardSize: Dp = min(availableWidthDp.value, availableHeightDp.value).dp

            // Calcular el tamaño de cada casilla (1/3 del tamaño total del tablero) en Dp
            val cellSize: Dp = boardSize / 3

            // Centrar el tablero real dentro de este BoxWithConstraints
            Box(
                modifier = Modifier
                    .size(boardSize)
                    .align(Alignment.Center)
            ) {
                Tablero(
                    tablero = tablero,
                    onCasillaClick = { fila, columna ->
                        if (!juegoTerminado) {
                            onCasillaClick(fila, columna)
                        }
                    },
                    cellSize = cellSize // Pasa el tamaño calculado a Tablero
                )
            }
        }
    }
}