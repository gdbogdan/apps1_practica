package com.example.tictactoe.resultados

import android.content.Context
import android.content.Intent
import com.example.tictactoe.R

fun enviarEmail(
    context: Context,
    fechaHoraFormateada: String,
    alias: String,
    tipoVictoria: String,
    dificultad: Boolean,
    temporizador: Boolean,
    minutosConfigurados: Int = 0,
    segundosConfigurados: Int = 0,
    minutosRestantes: Int = 0,
    segundosRestantes: Int = 0,
    casillasRestantes: Int,
    email: String
) {
    val asunto = "Log - $fechaHoraFormateada"

    //Calculo del tiempo correcto:
    val totalSegundosConfigurados = (minutosConfigurados * 60) + segundosConfigurados
    val totalSegundosRestantes = (minutosRestantes * 60) + segundosRestantes
    val tiempoEmpleadoSegundos = (totalSegundosConfigurados - totalSegundosRestantes).coerceAtLeast(0)
    val minutosEmpleados = tiempoEmpleadoSegundos / 60
    val segundosEmpleados = tiempoEmpleadoSegundos % 60

    val cuerpo = buildString {
        appendLine(tipoVictoria)
        appendLine(context.getString(R.string.alias_r, alias))
        appendLine(context.getString(R.string.casillas_restantes_r, casillasRestantes))
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
                    R.string.tiempo_juego_resumen,
                    minutosEmpleados,
                    segundosEmpleados,
                    minutosConfigurados,
                    segundosConfigurados
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
}