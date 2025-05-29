package com.example.tictactoe.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.tictactoe.R

@Composable
fun ImagenInicio(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.fillMaxWidth(0.6f),
        painter = painterResource(id = R.drawable.tic_tac_toe),
        contentDescription = stringResource(R.string.letrero)
    )
}
