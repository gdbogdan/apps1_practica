package com.example.tictactoe.view_models

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tictactoe.screens.Simbolo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JuegoViewModel : ViewModel() {

    // Estado del tablero (interno y externo)
    private val _tablero = mutableStateOf(Array(3) { Array(3) { Simbolo.Vacio } })
    val tablero: State<Array<Array<Simbolo>>> = _tablero

    // Turno (interno y externo)
    private val _turno = mutableStateOf(Simbolo.X)
    val turno: State<Simbolo> = _turno

    // Ganador (interno y externo)
    private val _ganador = mutableStateOf<Simbolo?>(null)
    val ganador: State<Simbolo?> = _ganador

    // Juego terminado (interno y externo)
    private val _juegoTerminado = mutableStateOf(false)
    val juegoTerminado: State<Boolean> = _juegoTerminado

    // Mostrar diálogo de ganador (interno y externo)
    private val _mostrarDialogoGanador = mutableStateOf(false)
    val mostrarDialogoGanador: State<Boolean> = _mostrarDialogoGanador

    // Mensaje del ganador (interno y externo)
    private val _mensajeGanador = mutableStateOf("")
    val mensajeGanador: State<String> = _mensajeGanador

    // Minutos del temporizador (interno y externo)
    private val _minutos = mutableIntStateOf(2)
    val minutos: State<Int> = _minutos

    // Segundos del temporizador (interno y  externo)
    private val _segundos = mutableIntStateOf(0)
    val segundos: State<Int> = _segundos

    private var temporizadorJob: Job? = null

    fun iniciarTemporizador() {
        temporizadorJob = CoroutineScope(Dispatchers.Default).launch {
            var totalSegundos = _minutos.value * 60 + _segundos.value
            while (totalSegundos >= 0 && !_juegoTerminado.value) {
                _minutos.value = totalSegundos / 60
                _segundos.value = totalSegundos % 60
                delay(1000)
                totalSegundos--
            }
            if (!_juegoTerminado.value) {
                _juegoTerminado.value = true
                _mensajeGanador.value = "¡Tiempo agotado! Empate."
                _mostrarDialogoGanador.value = true
            }
        }
    }

    fun detenerTemporizador() {
        temporizadorJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        detenerTemporizador()
    }

    fun reiniciarJuego() {
        _tablero.value = Array(3) { Array(3) { Simbolo.Vacio } }
        _turno.value = Simbolo.X
        _ganador.value = null
        _juegoTerminado.value = false
        _mostrarDialogoGanador.value = false
        _mensajeGanador.value = ""
        _minutos.value = 2
        _segundos.value = 0
        detenerTemporizador()
    }

    fun jugarCasilla(fila: Int, columna: Int, dificultad: Boolean) {
        if (_ganador.value != null || _juegoTerminado.value || _tablero.value[fila][columna] != Simbolo.Vacio) return

        val nuevoTablero = _tablero.value.copyOf().map { it.copyOf() }.toTypedArray()
        nuevoTablero[fila][columna] = _turno.value
        _tablero.value = nuevoTablero

        val posibleGanador = comprobarGanador(nuevoTablero)
        if (posibleGanador != null) {
            finalizarJuego(posibleGanador)
            detenerTemporizador()
        } else {
            _turno.value = if (_turno.value == Simbolo.X) Simbolo.O else Simbolo.X
            if (_turno.value == Simbolo.O && !_juegoTerminado.value) {
                moverIA(dificultad)
            }
        }
    }

    private fun moverIA(dificultad: Boolean) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000) // Esperar 2 segundos

            val movimiento = realizarMovimientoIA(_tablero.value, dificultad)
            movimiento?.let { (fila, columna) ->
                val nuevoTablero = _tablero.value.copyOf().map { it.copyOf() }.toTypedArray()
                nuevoTablero[fila][columna] = Simbolo.O
                _tablero.value = nuevoTablero
                val posibleGanador = comprobarGanador(nuevoTablero)
                if (posibleGanador != null) {
                    finalizarJuego(posibleGanador)
                    detenerTemporizador()
                } else {
                    _turno.value = Simbolo.X // El turno vuelve al jugador después del movimiento de la IA
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
            casillasVacias.random()
        } else {
            casillasVacias.first()
        }
    }
}