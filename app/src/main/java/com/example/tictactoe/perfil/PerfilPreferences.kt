package com.example.tictactoe.perfil

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class PerfilPreferences(
    val alias: String,
    val dificultad: Boolean,
    val temporizador: Boolean,
    val minutos: Int,
    val segundos: Int,
    val primerJuego: Boolean,
    val email: String,
    val minutosRestantes: Int,
    val segundosRestantes: Int
)

class PerfilPreferencesRepository (private val context: Context){
    private object PreferencesKeys {
        val ALIAS = stringPreferencesKey("alias")
        val DIFICULTAD = booleanPreferencesKey("dificultad")
        val TEMPORIZADOR = booleanPreferencesKey("temporizador")
        val MINUTOS = intPreferencesKey("minutos")
        val SEGUNDOS = intPreferencesKey("segundos")
        val PRIMER_JUEGO = booleanPreferencesKey("primer_juego")
        val EMAIL = stringPreferencesKey("email") // Se mantiene
        val MINUTOS_RESTANTES_PREF = intPreferencesKey("minutos_restantes")
        val SEGUNDOS_RESTANTES_PREF = intPreferencesKey("segundos_restantes")
    }

    val userPreferencesFlow: Flow<PerfilPreferences> = context.dataStore.data
        .map { preferences ->
            PerfilPreferences(
                alias = preferences[PreferencesKeys.ALIAS] ?: "",
                dificultad = preferences[PreferencesKeys.DIFICULTAD] ?: false,
                temporizador = preferences[PreferencesKeys.TEMPORIZADOR] ?: false,
                minutos = preferences[PreferencesKeys.MINUTOS] ?: 0,
                segundos = preferences[PreferencesKeys.SEGUNDOS] ?: 0,
                primerJuego = preferences[PreferencesKeys.PRIMER_JUEGO] ?: true,
                email = preferences[PreferencesKeys.EMAIL] ?: "", // Se mantiene
                minutosRestantes = preferences[PreferencesKeys.MINUTOS_RESTANTES_PREF] ?: 0,
                segundosRestantes = preferences[PreferencesKeys.SEGUNDOS_RESTANTES_PREF] ?: 0
            )
        }

    suspend fun updateAlias(alias: String) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.ALIAS] = alias }
    }

    suspend fun updateDificultad(dificultad: Boolean) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.DIFICULTAD] = dificultad }
    }

    suspend fun updateTemporizador(temporizador: Boolean) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.TEMPORIZADOR] = temporizador }
    }

    suspend fun updateMinutos(minutos: Int) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.MINUTOS] = minutos }
    }

    suspend fun updateSegundos(segundos: Int) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.SEGUNDOS] = segundos }
    }

    suspend fun updatePrimerJuego(primerJuego: Boolean) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.PRIMER_JUEGO] = primerJuego }
    }

    suspend fun updateEmail(email: String) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.EMAIL] = email }
    }

    suspend fun updateMinutosRestantes(minutos: Int) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.MINUTOS_RESTANTES_PREF] = minutos }
    }

    suspend fun updateSegundosRestantes(segundos: Int) {
        context.dataStore.edit { preferences -> preferences[PreferencesKeys.SEGUNDOS_RESTANTES_PREF] = segundos }
    }
}