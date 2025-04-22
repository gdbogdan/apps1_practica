package com.example.tictactoe.view_models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tictactoe.screens.Simbolo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class JugarViewModel : ViewModel() {

    private val _tablero = mutableStateOf(Array(3) { Array(3) { Simbolo.Vacio } })
    val tablero: State<Array<Array<Simbolo>>> = _tablero

    private val _turno = mutableStateOf(Simbolo.X)
    val turno: State<Simbolo> = _turno

    private val _ganador = mutableStateOf<Simbolo?>(null)
    val ganador: State<Simbolo?> = _ganador

    private val _juegoTerminado = mutableStateOf(false)
    val juegoTerminado: State<Boolean> = _juegoTerminado

    private val _mostrarDialogoGanador = mutableStateOf(false)
    val mostrarDialogoGanador: State<Boolean> = _mostrarDialogoGanador

    private val _mensajeGanador = mutableStateOf("")
    val mensajeGanador: State<String> = _mensajeGanador

    fun reiniciarJuego() {
        _tablero.value = Array(3) { Array(3) { Simbolo.Vacio } }
        _turno.value = Simbolo.X
        _ganador.value = null
        _juegoTerminado.value = false
        _mostrarDialogoGanador.value = false
        _mensajeGanador.value = ""
    }

    fun jugarCasilla(fila: Int, columna: Int, dificultad: Boolean) {
        if (_ganador.value != null || _juegoTerminado.value || _tablero.value[fila][columna] != Simbolo.Vacio) return

        val nuevoTablero = _tablero.value.copyOf().map { it.copyOf() }.toTypedArray()
        nuevoTablero[fila][columna] = _turno.value
        _tablero.value = nuevoTablero

        val posibleGanador = comprobarGanador(nuevoTablero)
        if (posibleGanador != null) {
            finalizarJuego(posibleGanador)
        } else {
            _turno.value = if (_turno.value == Simbolo.X) Simbolo.O else Simbolo.X
            if (_turno.value == Simbolo.O && !_juegoTerminado.value) {
                moverIA(dificultad)
            }
        }
    }

    private fun moverIA(dificultad: Boolean) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000) // Reducir la espera para una respuesta más ágil

            val movimiento = realizarMovimientoIA(_tablero.value, dificultad)
            movimiento?.let { (fila, columna) ->
                val nuevoTablero = _tablero.value.copyOf().map { it.copyOf() }.toTypedArray()
                nuevoTablero[fila][columna] = Simbolo.O
                _tablero.value = nuevoTablero
                val posibleGanador = comprobarGanador(nuevoTablero)
                if (posibleGanador != null) {
                    finalizarJuego(posibleGanador)
                } else {
                    _turno.value = Simbolo.X
                }
            }
        }
    }

    private fun finalizarJuego(simboloGanador: Simbolo) {
        _ganador.value = simboloGanador
        _juegoTerminado.value = true
        _mensajeGanador.value = when (simboloGanador) {
            Simbolo.X -> "¡Has ganado!"
            Simbolo.O -> "¡Has perdido!"
            Simbolo.Vacio -> "¡Empate!"
        }
        _mostrarDialogoGanador.value = true
    }

    fun finalizarJuegoPorTiempoAgotado() {
        _juegoTerminado.value = true
        _mensajeGanador.value = "¡Tiempo agotado! Has perdido."
        _mostrarDialogoGanador.value = true
    }

    private fun comprobarGanador(tablero: Array<Array<Simbolo>>): Simbolo? {
        // Comprobar filas
        for (fila in tablero) {
            if (fila[0] != Simbolo.Vacio && fila[0] == fila[1] && fila[1] == fila[2]) {
                return fila[0]
            }
        }

        // Comprobar columnas
        for (columna in tablero[0].indices) {
            if (tablero[0][columna] != Simbolo.Vacio &&
                tablero[0][columna] == tablero[1][columna] &&
                tablero[1][columna] == tablero[2][columna]
            ) {
                return tablero[0][columna]
            }
        }

        // Comprobar diagonales
        if (tablero[0][0] != Simbolo.Vacio &&
            tablero[0][0] == tablero[1][1] &&
            tablero[1][1] == tablero[2][2]
        ) {
            return tablero[0][0]
        }

        if (tablero[0][2] != Simbolo.Vacio &&
            tablero[0][2] == tablero[1][1] &&
            tablero[1][1] == tablero[2][0]
        ) {
            return tablero[0][2]
        }

        // Comprobar si hay empate
        if (tablero.all { fila -> fila.all { it != Simbolo.Vacio } }) {
            return Simbolo.Vacio // Empate
        }

        return null // No hay ganador aún
    }

    private fun realizarMovimientoIA(tablero: Array<Array<Simbolo>>, dificultad: Boolean): Pair<Int, Int>? {
        val casillasVacias = mutableListOf<Pair<Int, Int>>()
        for (fila in tablero.indices) {
            for (columna in tablero[fila].indices) {
                if (tablero[fila][columna] == Simbolo.Vacio) {
                    casillasVacias.add(Pair(fila, columna))
                }
            }
        }

        if (casillasVacias.isEmpty()) return null

        return if (dificultad) {
            // 1. Intentar ganar
            for ((fila, columna) in casillasVacias) {
                val tableroTemporal = tablero.copyOf().map { it.copyOf() }.toTypedArray()
                tableroTemporal[fila][columna] = Simbolo.O
                if (comprobarGanador(tableroTemporal) == Simbolo.O) {
                    return Pair(fila, columna)
                }
            }

            // 2. Bloquear al jugador
            for ((fila, columna) in casillasVacias) {
                val tableroTemporal = tablero.copyOf().map { it.copyOf() }.toTypedArray()
                tableroTemporal[fila][columna] = Simbolo.X
                if (comprobarGanador(tableroTemporal) == Simbolo.X) {
                    return Pair(fila, columna)
                }
            }

            // 3. Intentar tomar el centro si está libre
            if (tablero[1][1] == Simbolo.Vacio) {
                return Pair(1, 1)
            }

            // 4. Intentar tomar esquinas si están libres
            val esquinasLibres = casillasVacias.filter { (fila, columna) ->
                (fila == 0 && columna == 0) || (fila == 0 && columna == 2) ||
                        (fila == 2 && columna == 0) || (fila == 2 && columna == 2)
            }
            if (esquinasLibres.isNotEmpty()) {
                return esquinasLibres.random()
            }

            // 5. Si no hay movimientos estratégicos, elegir una casilla vacía al azar
            casillasVacias.random()
        } else {
            casillasVacias.random()
        }
    }
}