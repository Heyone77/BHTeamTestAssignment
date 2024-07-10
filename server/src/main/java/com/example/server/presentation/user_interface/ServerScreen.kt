package com.example.server.presentation.user_interface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.server.presentation.viewmodels.ServerViewModel

@Composable
fun ServerScreen(viewModel: ServerViewModel = hiltViewModel()) {
    val isServerRunning by viewModel.isServerRunning.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { viewModel.toggleServer() }) {
            Text(text = if (isServerRunning) "Stop Server" else "Start Server")
        }
        Button(onClick = { viewModel.openConfigDialog() }) {
            Text("Configure Server")
        }
    }

    if (viewModel.showConfigDialog) {
        ConfigDialog(viewModel)
    }
}