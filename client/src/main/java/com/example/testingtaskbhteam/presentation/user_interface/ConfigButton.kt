package com.example.testingtaskbhteam.presentation.user_interface

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ConfigButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Config")
    }
}