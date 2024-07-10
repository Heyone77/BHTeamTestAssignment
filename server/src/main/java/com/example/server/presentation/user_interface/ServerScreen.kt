package com.example.server.presentation.user_interface

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.server.data.TouchData
import com.example.server.presentation.viewmodels.ServerViewModel

@Composable
fun ServerScreen(viewModel: ServerViewModel = hiltViewModel()) {
    val isServerRunning by viewModel.isServerRunning.collectAsState()
    val touchDataList by viewModel.touchDataList.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (touchDataList.isNotEmpty()) {
                for (i in 1 until touchDataList.size) {
                    val start = touchDataList[i - 1]
                    val end = touchDataList[i]
                    drawLine(
                        color = Color.Red.copy(alpha = end.pressure),
                        start = Offset(start.x, start.y),
                        end = Offset(end.x, end.y),
                        strokeWidth = 5f
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
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
            Button(onClick = { viewModel.clearData() }) {
                Text("Clear Data")
            }
        }

        if (viewModel.showConfigDialog) {
            ConfigDialog(viewModel)
        }
    }
}

fun DrawScope.drawTrack(touchDataList: List<TouchData>) {
    for (i in 1 until touchDataList.size) {
        val start = touchDataList[i - 1]
        val end = touchDataList[i]
        drawLine(
            color = Color.Red.copy(alpha = end.pressure),
            start = androidx.compose.ui.geometry.Offset(start.x, start.y),
            end = androidx.compose.ui.geometry.Offset(end.x, end.y),
            strokeWidth = 10f
        )
    }
}