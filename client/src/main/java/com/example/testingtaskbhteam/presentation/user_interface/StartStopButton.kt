package com.example.testingtaskbhteam.presentation.user_interface

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartStopButton(isStarted: Boolean, onStateChange: (Boolean) -> Unit) {
    Button(
        onClick = { onStateChange(!isStarted) },
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(text = if (isStarted) "Стоп" else "Старт")
    }
}