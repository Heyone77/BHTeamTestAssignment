package com.example.server.presentation.user_interface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.server.presentation.viewmodels.ServerViewModel


@Composable
fun ServerScreen(viewModel: ServerViewModel) {
    val isRunning by viewModel.isRunning.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ConfigButton { viewModel.openConfigDialog() }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.toggleServer() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = if (isRunning) "Выключить" else "Включить")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Показать полный трек */ },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Полный трек")
        }
    }

    if (viewModel.showConfigDialog) {
        ConfigDialog(viewModel)
    }
}

@Composable
fun ConfigButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Config")
    }
}