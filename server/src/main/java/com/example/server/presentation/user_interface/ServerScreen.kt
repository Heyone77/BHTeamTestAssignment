package com.example.server.presentation.user_interface

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.toggleServer() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isServerRunning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (isServerRunning) "Остановить сервер" else "Запустить сервер")
            }
            Button(
                onClick = { viewModel.openConfigDialog() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Настроить сервер")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.replayTouchData { touchData ->
                            viewModel.updateReplayTrack(touchData)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Повторить трек")
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

            val alpha = end.pressure.coerceIn(0f, 1f)

            drawLine(
                color = Color.Red.copy(alpha = alpha),
                start = Offset(start.x, start.y),
                end = Offset(end.x, end.y),
                strokeWidth = 5f
            )
        }
    }
}
