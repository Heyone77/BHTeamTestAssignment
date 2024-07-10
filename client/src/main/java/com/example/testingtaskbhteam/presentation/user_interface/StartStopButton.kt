package com.example.testingtaskbhteam.presentation.user_interface

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StartStopButton(isStarted: Boolean, onStateChange: () -> Unit) {
    Button(onClick = onStateChange) {
        Text(text = if (isStarted) "Stop" else "Start")
    }
}