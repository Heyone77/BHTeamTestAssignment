package com.example.testingtaskbhteam.presentation.user_interface


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testingtaskbhteam.presentation.viewmodels.ClientViewModel

@Composable
fun ClientScreen(viewModel: ClientViewModel) {
    val isStarted by viewModel.isStarted.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StartStopButton(isStarted = isStarted) { viewModel.toggleStartStop() }
        Spacer(modifier = Modifier.height(16.dp))
        ConfigButton { viewModel.openConfigDialog() }
    }

    if (viewModel.showConfigDialog) {
        ConfigDialog(viewModel)
    }
}





