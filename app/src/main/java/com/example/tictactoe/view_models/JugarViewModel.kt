package com.example.tictactoe.view_models

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.resultados.ResultadoDetallado
import com.example.tictactoe.resultados.ResultadoJuego
import com.example.tictactoe.screens.Simbolo
import com.example.tictactoe.screens.TipoVictoria
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.tictactoe.R

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

    private val _resultado = mutableStateOf<ResultadoJuego?>(null)
    val resultado: State<ResultadoJuego?> = _resultado

    private val _tipoVictoria = mutableStateOf<TipoVictoria?>(null)
    val tipoVictoria: State<TipoVictoria?> = _tipoVictoria

    fun obtenerMensajeVictoriaFormateado(context: Context): String {
        return when (_resultado.value) {
            ResultadoJuego.GANASTE -> when (_tipoVictoria.value) {
                TipoVictoria.FILA1 -> context.getString(R.string.victoria_fila, 1)
                TipoVictoria.FILA2 -> context.getString(R.string.victoria_fila, 2)
                TipoVictoria.FILA3 -> context.getString(R.string.victoria_fila, 3)
                TipoVictoria.COLUMNA1 -> context.getString(R.string.victoria_columna, 1)
                TipoVictoria.COLUMNA2 -> context.getString(R.string.victoria_columna, 2)
                TipoVictoria.COLUMNA3 -> context.getString(R.string.victoria_columna, 3)
                TipoVictoria.DIAGONAL1 -> context.getString(R.string.victoria_diagonal1)
                TipoVictoria.DIAGONAL2 -> context.getString(R.string.victoria_diagonal2)
                TipoVictoria.EMPATE -> context.getString(R.string.empate)
                null -> context.getString(R.string.sin_victoria)
            }

            ResultadoJuego.PERDISTE -> context.getString(R.string.has_perdido)
            ResultadoJuego.EMPATE -> context.getString(R.string.empate)
            ResultadoJuego.TIEMPO_AGOTADO -> context.getString(R.string.tiempo_agotado)
            null -> context.getString(R.string.sin_victoria)
        }
    }

    fun reiniciarJuego() {
        _tablero.value = Array(3) { Array(3) { Simbolo.Vacio } }
        _turno.value = Simbolo.X
        _ganador.value = null
        _tipoVictoria.value = null
        _juegoTerminado.value = false
        _mostrarDialogoGanador.value = false
        _resultado.value = null
    }

    fun jugarCasilla(fila: Int, columna: Int, dificultad: Boolean) {
        if (_ganador.value != null || _juegoTerminado.value || _tablero.value[fila][columna] != Simbolo.Vacio) return

        val nuevoTablero = _tablero.value.copyOf().map { it.copyOf() }.toTypedArray()
        nuevoTablero[fila][columna] = _turno.value
        _tablero.value = nuevoTablero

        val resultadoDetallado = comprobarGanadorDetallado(nuevoTablero)
        val posibleGanador = resultadoDetallado?.simbolo

        if (posibleGanador != null) {
            finalizarJuego(resultadoDetallado)
        } else {
            _turno.value = if (_turno.value == Simbolo.X) Simbolo.O else Simbolo.X
            if (_turno.value == Simbolo.O && !_juegoTerminado.value) {
                moverIA(dificultad)
            }
        }
    }

    private fun moverIA(dificultad: Boolean) {
        viewModelScope.launch {
            delay(1000) // Espera de 1 segundo

            val movimiento = realizarMovimientoIA(_tablero.value, dificultad)
            movimiento?.let { (fila, columna) ->
                val nuevoTablero = _tablero.value.copyOf().map { it.copyOf() }.toTypedArray()
                nuevoTablero[fila][columna] = Simbolo.O
                _tablero.value = nuevoTablero

                val resultadoDetallado = comprobarGanadorDetallado(nuevoTablero)
                val posibleGanador = resultadoDetallado?.simbolo

                if (posibleGanador != null) {
                    finalizarJuego(resultadoDetallado)
                } else {
                    _turno.value = Simbolo.X
                }
            }
        }
    }


    private fun finalizarJuego(resultadoDetallado: ResultadoDetallado) {
        _ganador.value = resultadoDetallado.simbolo
        _tipoVictoria.value = resultadoDetallado.tipo

        _juegoTerminado.value = true
        _resultado.value = when (resultadoDetallado.simbolo) {
            Simbolo.X -> ResultadoJuego.GANASTE
            Simbolo.O -> ResultadoJuego.PERDISTE
            Simbolo.Vacio -> ResultadoJuego.EMPATE
        }
        _mostrarDialogoGanador.value = true
    }


    fun finalizarJuegoPorTiempoAgotado() {
        _ganador.value = Simbolo.Vacio
        _tipoVictoria.value = null
        _juegoTerminado.value = true
        _resultado.value = ResultadoJuego.TIEMPO_AGOTADO
        _mostrarDialogoGanador.value = true
    }

    private fun comprobarGanadorDetallado(tablero: Array<Array<Simbolo>>): ResultadoDetallado? {
        // Comprobar filas
        for (i in 0..2) {
            if (tablero[i][0] != Simbolo.Vacio &&
                tablero[i][0] == tablero[i][1] &&
                tablero[i][1] == tablero[i][2]) {
                return ResultadoDetallado(tablero[i][0], TipoVictoria.valueOf("FILA${i + 1}"))
            }
        }

        // Comprobar columnas
        for (j in 0..2) {
            if (tablero[0][j] != Simbolo.Vacio &&
                tablero[0][j] == tablero[1][j] &&
                tablero[1][j] == tablero[2][j]) {
                return ResultadoDetallado(tablero[0][j], TipoVictoria.valueOf("COLUMNA${j + 1}"))
            }
        }

        // Diagonal principal
        if (tablero[0][0] != Simbolo.Vacio &&
            tablero[0][0] == tablero[1][1] &&
            tablero[1][1] == tablero[2][2]) {
            return ResultadoDetallado(tablero[0][0], TipoVictoria.DIAGONAL1)
        }

        // Diagonal secundaria
        if (tablero[0][2] != Simbolo.Vacio &&
            tablero[0][2] == tablero[1][1] &&
            tablero[1][1] == tablero[2][0]) {
            return ResultadoDetallado(tablero[0][2], TipoVictoria.DIAGONAL2)
        }

        // Empate
        if (tablero.all { fila -> fila.all { it != Simbolo.Vacio } }) {
            return ResultadoDetallado(Simbolo.Vacio, TipoVictoria.EMPATE)
        }

        return null
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
                val resultado = comprobarGanadorDetallado(tableroTemporal)
                if (resultado?.simbolo == Simbolo.O) {
                    return Pair(fila, columna)
                }
            }

            // 2. Bloquear al jugador
            for ((fila, columna) in casillasVacias) {
                val tableroTemporal = tablero.copyOf().map { it.copyOf() }.toTypedArray()
                tableroTemporal[fila][columna] = Simbolo.X
                val resultado = comprobarGanadorDetallado(tableroTemporal)
                if (resultado?.simbolo == Simbolo.X) {
                    return Pair(fila, columna)
                }
            }

            // 3. Centro
            if (tablero[1][1] == Simbolo.Vacio) return Pair(1, 1)

            // 4. Esquinas
            val esquinasLibres = casillasVacias.filter { (fila, columna) ->
                (fila == 0 && columna == 0) || (fila == 0 && columna == 2) ||
                        (fila == 2 && columna == 0) || (fila == 2 && columna == 2)
            }
            if (esquinasLibres.isNotEmpty()) {
                return esquinasLibres.random()
            }

            // 5. Cualquiera
            casillasVacias.random()
        } else {
            casillasVacias.random()
        }
    }

}