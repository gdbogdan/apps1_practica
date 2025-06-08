package com.example.tictactoe.jugar

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.R
import com.example.tictactoe.database.Partida
import com.example.tictactoe.database.PartidasRepository
import com.example.tictactoe.resultados.ResultadoDetallado
import com.example.tictactoe.resultados.ResultadoJuego
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import android.os.CountDownTimer
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class JugarViewModel(
    private val partidasRepository: PartidasRepository
) : ViewModel() {

    private val _tablero = mutableStateOf(List(3) { List(3) { Simbolo.Vacio } })
    val tablero: State<List<List<Simbolo>>> = _tablero

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

    // Contador de toques en casillas ocupadas: Map<Pair<fila, columna>, contador>
    private val _toquesCasillaOcupada = mutableStateOf(mutableMapOf<Pair<Int, Int>, Int>())

    // SharedFlow para enviar eventos de feedback a la UI (mensajes)
    private val _feedbackMessage = MutableSharedFlow<String>()
    val feedbackMessage: SharedFlow<String> = _feedbackMessage

    // SharedFlow para enviar el evento de cerrar la app
    private val _closeAppEvent = MutableSharedFlow<Unit>()
    val closeAppEvent: SharedFlow<Unit> = _closeAppEvent

    private val _casillasRestantes = mutableStateOf(9)
    val casillasRestantes: State<Int> = _casillasRestantes


    var dificultad by mutableStateOf(false)
    var temporizadorActivado by mutableStateOf(false)
    var tiempoConfiguradoMinutos by mutableIntStateOf(0)
    var tiempoConfiguradoSegundos by mutableIntStateOf(0)
    var tiempoRestanteMinutos by mutableIntStateOf(0)
    var tiempoRestanteSegundos by mutableIntStateOf(0)

    private var currentTimer: CountDownTimer? = null

    fun guardarPartida(aliasJugador: String) {
        val resultadoFinal = _resultado.value?.name ?: "DESCONOCIDO"

        val madridZoneId = ZoneId.of("Europe/Madrid")
        val fechaHoraActual = LocalDateTime.now(madridZoneId)
        val formatoFechaHora = DateTimeFormatter.ofPattern("'Fecha: ' dd/MM/yyyy ' - Hora: ' HH:mm")
        val fechaHoraFormateada = fechaHoraActual.format(formatoFechaHora)

        val partidaAGuardar = Partida(
            alias = aliasJugador,
            fechaHoraFormateada = fechaHoraFormateada,
            resultado = resultadoFinal,
            dificultad = dificultad,
            temporizador = temporizadorActivado,
            minutosConfigurados = tiempoConfiguradoMinutos,
            segundosConfigurados = tiempoConfiguradoSegundos,
            minutosRestantes = tiempoRestanteMinutos,
            segundosRestantes = tiempoRestanteSegundos,
            casillasRestantes = _casillasRestantes.value
        )

        viewModelScope.launch {
            partidasRepository.insertPartida(partidaAGuardar)
            println("Partida guardada: $partidaAGuardar")
        }
    }

    // --- Funciones para iniciar el temporizador (asumiendo que las tienes en otro sitio) ---
    fun iniciarTemporizador(minutos: Int, segundos: Int) {
        tiempoConfiguradoMinutos = minutos
        tiempoConfiguradoSegundos = segundos
        tiempoRestanteMinutos = minutos
        tiempoRestanteSegundos = segundos

        val totalMillis = (minutos * 60 + segundos) * 1000L
        currentTimer?.cancel()

        currentTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestanteMinutos = (millisUntilFinished / 1000 / 60).toInt()
                tiempoRestanteSegundos = (millisUntilFinished / 1000 % 60).toInt()
            }

            override fun onFinish() {
                // Asegúrate de que el resultado no haya sido ya establecido (victoria/empate)
                if (_juegoTerminado.value.not()) {
                    finalizarJuegoPorTiempoAgotado()
                    // La llamada a guardarPartida se hará desde Resultados.kt
                }
            }
        }.start()
    }

    fun detenerTemporizador() {
        currentTimer?.cancel()
    }

    // --- MODIFICACIONES EN reiniciarJuego() ---
    fun reiniciarJuego() {
        _tablero.value = List(3) { List(3) { Simbolo.Vacio } }
        _turno.value = Simbolo.X
        _ganador.value = null
        _tipoVictoria.value = null
        _juegoTerminado.value = false
        _mostrarDialogoGanador.value = false
        _resultado.value = null
        _toquesCasillaOcupada.value.clear()
        _casillasRestantes.value = 9
        // Reiniciar variables del temporizador
        detenerTemporizador()
        temporizadorActivado = false
        tiempoConfiguradoMinutos = 0
        tiempoConfiguradoSegundos = 0
        tiempoRestanteMinutos = 0
        tiempoRestanteSegundos = 0
    }

    suspend fun onCasillaClick(fila: Int, columna: Int, context: Context): Boolean { // Eliminar dificultad de aquí, se usa desde la propiedad de clase
        val simboloEnCasillaAntes = _tablero.value[fila][columna]
        val casillaKey = Pair(fila, columna)

        if (_juegoTerminado.value) return false

        if (simboloEnCasillaAntes == Simbolo.Vacio) {
            _toquesCasillaOcupada.value.remove(casillaKey)

            val simboloDelJugadorQueMueve = _turno.value

            jugarCasilla(fila, columna, context)
            _feedbackMessage.emit(
                context.getString(
                    R.string.simbolo_colocado_en_casilla,
                    simboloDelJugadorQueMueve.name,
                    fila + 1,
                    columna + 1
                )
            )
            return true
        } else {
            val contador = _toquesCasillaOcupada.value.getOrDefault(casillaKey, 0) + 1
            _toquesCasillaOcupada.value[casillaKey] = contador

            when (contador) {
                1 -> _feedbackMessage.emit(context.getString(R.string.primer_aviso_casilla_ocupada))
                2 -> _feedbackMessage.emit(context.getString(R.string.ultimo_aviso_casilla_ocupada))
                else -> {
                    _feedbackMessage.emit(context.getString(R.string.te_lo_avise))
                    _closeAppEvent.emit(Unit)
                }
            }
            return false
        }
    }

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


    fun jugarCasilla(fila: Int, columna: Int, context: Context) { // Eliminada 'dificultad' de los parámetros
        if (_ganador.value != null || _juegoTerminado.value || _tablero.value[fila][columna] != Simbolo.Vacio) return

        val nuevoTablero = _tablero.value.toMutableList().apply {
            this[fila] = this[fila].toMutableList().apply {
                this[columna] = _turno.value
            }.toList()
        }.toList()

        _tablero.value = nuevoTablero
        _casillasRestantes.value--

        val resultadoDetallado = comprobarGanadorDetallado(nuevoTablero)
        val posibleGanador = resultadoDetallado?.simbolo

        if (posibleGanador != null) {
            finalizarJuego(resultadoDetallado)
        } else {
            _turno.value = if (_turno.value == Simbolo.X) Simbolo.O else Simbolo.X
            if (_turno.value == Simbolo.O && !_juegoTerminado.value) {
                moverIA(context) // Llamada a moverIA sin 'dificultad' en parámetros
            }
        }
    }

    private fun moverIA(context: Context) { // Eliminada 'dificultad' de los parámetros, ahora usa la propiedad de clase
        viewModelScope.launch {
            delay(2000)

            val movimiento = realizarMovimientoIA(_tablero.value, dificultad) // Usa la propiedad de clase 'dificultad'
            movimiento?.let { (fila, columna) ->
                val nuevoTablero = _tablero.value.toMutableList().apply {
                    this[fila] = this[fila].toMutableList().apply {
                        this[columna] = Simbolo.O
                    }.toList()
                }.toList()

                _tablero.value = nuevoTablero
                _casillasRestantes.value--

                _feedbackMessage.emit(
                    context.getString(
                        R.string.simbolo_colocado_en_casilla,
                        Simbolo.O.name,
                        fila + 1,
                        columna + 1
                    )
                )

                val resultadoDetallado = comprobarGanadorDetallado(nuevoTablero)
                val posibleGanador = resultadoDetallado?.simbolo

                if (posibleGanador != null) {
                    finalizarJuego(resultadoDetallado)
                } else {
                    _turno.value = Simbolo.X
                }
            } ?: run {
                // Si la IA no puede moverse (ej. tablero lleno sin ganador), considera un empate.
                // Esto podría ser redundante si checkDraw ya lo gestiona, pero es una buena medida.
                if (_casillasRestantes.value == 0 && _ganador.value == null) {
                    finalizarJuego(ResultadoDetallado(Simbolo.Vacio, TipoVictoria.EMPATE))
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

        // Llamar a guardarPartida cuando el juego ha finalizado
        // El alias se pasará desde la UI (Resultados.kt)
        // No llamamos aquí directamente para que sea la UI la que active el guardado tras mostrar el resultado
    }

    fun finalizarJuegoPorTiempoAgotado() {
        _ganador.value = Simbolo.Vacio
        _tipoVictoria.value = null
        _juegoTerminado.value = true
        _resultado.value = ResultadoJuego.TIEMPO_AGOTADO
        _mostrarDialogoGanador.value = true

        // Llamar a guardarPartida cuando el juego ha finalizado
        // El alias se pasará desde la UI (Resultados.kt)
    }

    private fun comprobarGanadorDetallado(tablero: List<List<Simbolo>>): ResultadoDetallado? {
        // ... (Tu código para comprobar ganador, sin cambios aquí) ...
        for (i in 0..2) {
            if (tablero[i][0] != Simbolo.Vacio &&
                tablero[i][0] == tablero[i][1] &&
                tablero[i][1] == tablero[i][2]) {
                return ResultadoDetallado(tablero[i][0], TipoVictoria.valueOf("FILA${i + 1}"))
            }
        }

        for (j in 0..2) {
            if (tablero[0][j] != Simbolo.Vacio &&
                tablero[0][j] == tablero[1][j] &&
                tablero[1][j] == tablero[2][j]) {
                return ResultadoDetallado(tablero[0][j], TipoVictoria.valueOf("COLUMNA${j + 1}"))
            }
        }

        if (tablero[0][0] != Simbolo.Vacio &&
            tablero[0][0] == tablero[1][1] &&
            tablero[1][1] == tablero[2][2]) {
            return ResultadoDetallado(tablero[0][0], TipoVictoria.DIAGONAL1)
        }

        if (tablero[0][2] != Simbolo.Vacio &&
            tablero[0][2] == tablero[1][1] &&
            tablero[1][1] == tablero[2][0]) {
            return ResultadoDetallado(tablero[0][2], TipoVictoria.DIAGONAL2)
        }

        // Empate - Asegúrate de que el empate se detecte solo si no hay ganador
        if (_casillasRestantes.value == 0 && _ganador.value == null) {
            return ResultadoDetallado(Simbolo.Vacio, TipoVictoria.EMPATE)
        }

        return null
    }


    private fun realizarMovimientoIA(tablero: List<List<Simbolo>>, dificultad: Boolean): Pair<Int, Int>? {
        val casillasVacias = mutableListOf<Pair<Int, Int>>()
        for (fila in tablero.indices) {
            for (columna in tablero[fila].indices) {
                if (tablero[fila][columna] == Simbolo.Vacio) {
                    casillasVacias.add(Pair(fila, columna))
                }
            }
        }

        if (casillasVacias.isEmpty()) return null

        return if (dificultad) { // Usa la propiedad de clase 'dificultad'
            // Lógica de IA difícil
            for ((fila, columna) in casillasVacias) {
                val tableroTemporal = tablero.toMutableList().apply {
                    this[fila] = this[fila].toMutableList().apply {
                        this[columna] = Simbolo.O
                    }.toList()
                }.toList()
                val resultado = comprobarGanadorDetallado(tableroTemporal)
                if (resultado?.simbolo == Simbolo.O) {
                    return Pair(fila, columna) // Mueve para ganar
                }
            }

            for ((fila, columna) in casillasVacias) {
                val tableroTemporal = tablero.toMutableList().apply {
                    this[fila] = this[fila].toMutableList().apply {
                        this[columna] = Simbolo.X
                    }.toList()
                }.toList()
                val resultado = comprobarGanadorDetallado(tableroTemporal)
                if (resultado?.simbolo == Simbolo.X) {
                    return Pair(fila, columna) // Bloquea al jugador
                }
            }

            // Ocupar el centro
            if (tablero[1][1] == Simbolo.Vacio) return Pair(1, 1)

            // Ocupar esquinas
            val esquinasLibres = casillasVacias.filter { (fila, columna) ->
                (fila == 0 && columna == 0) || (fila == 0 && columna == 2) ||
                        (fila == 2 && columna == 0) || (fila == 2 && columna == 2)
            }
            if (esquinasLibres.isNotEmpty()) {
                return esquinasLibres.random()
            }

            casillasVacias.random() // Si no hay mejores opciones, elige al azar
        } else {
            // Lógica de IA fácil (aleatorio)
            casillasVacias.random()
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentTimer?.cancel() // Cancela el temporizador al limpiar el ViewModel
    }
}

// --- Factory para JugarViewModel ---
// Es necesario para inyectar PartidasRepository
class JugarViewModelFactory(private val partidasRepository: PartidasRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JugarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JugarViewModel(partidasRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}