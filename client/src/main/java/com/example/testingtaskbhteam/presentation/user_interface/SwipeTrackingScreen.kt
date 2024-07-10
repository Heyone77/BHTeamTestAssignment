package com.example.testingtaskbhteam.presentation.user_interface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.testingtaskbhteam.data.TouchData
import com.example.testingtaskbhteam.presentation.viewmodels.ClientViewModel
import kotlinx.coroutines.launch

@Composable
fun SwipeTrackingScreen(viewModel: ClientViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val isStarted by viewModel.isStarted.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        for (change in event.changes) {
                            if (change.pressed) {
                                coroutineScope.launch {
                                    handleTouch(
                                        viewModel,
                                        change.position.x,
                                        change.position.y,
                                        change.pressure
                                    )
                                }
                            }
                        }
                    }
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.toggleStartStop() }) {
            Text(text = if (isStarted) "Stop" else "Start")
        }
    }
}

private fun handleTouch(viewModel: ClientViewModel, x: Float, y: Float, pressure: Float) {
    val touchData = TouchData(
        x = x,
        y = y,
        pressure = pressure,
        timestamp = System.currentTimeMillis()
    )
    println("Handling touch: $touchData")
    viewModel.updateTouchData(touchData)
}
