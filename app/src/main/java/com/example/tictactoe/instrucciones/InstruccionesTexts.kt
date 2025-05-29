package com.example.tictactoe.instrucciones

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tictactoe.R

@Composable
fun InstruccionesTexts() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(R.string.instrucciones),
                style = TextStyle(textAlign = TextAlign.Justify)
            )
        }

        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(R.string.instrucciones_perfil),
                style = TextStyle(textAlign = TextAlign.Justify)
            )
        }
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = stringResource(R.string.instrucciones_dificultad),
                style = TextStyle(textAlign = TextAlign.Justify)
            )
        }

        Row {
            Text(
                text = stringResource(R.string.instrucciones_navegacion),
                style = TextStyle(textAlign = TextAlign.Justify)
            )
        }
    }
}