package com.example.server.presentation.user_interface

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.server.data.TouchData
import com.example.server.presentation.viewmodels.ServerViewModel
import kotlinx.coroutines.launch

@Composable
fun ServerScreen(viewModel: ServerViewModel = hiltViewModel()) {
    val isServerRunning by viewModel.isServerRunning.collectAsState()
    val touchDataList by viewModel.touchDataList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawTrack(touchDataList)
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
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
            Button(onClick = {
                coroutineScope.launch {
                    viewModel.replayTouchData { touchData ->
                        viewModel.updateReplayTrack(touchData)
                    }
                }
            }) {
                Text("Replay Track")
            }
        }
    }

    if (viewModel.showConfigDialog) {
        ConfigDialog(viewModel)
    }
}

fun DrawScope.drawTrack(touchDataList: List<TouchData>) {
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
