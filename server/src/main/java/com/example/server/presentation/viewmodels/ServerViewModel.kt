package com.example.server.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.server.data.TouchData
import com.example.server.data.TouchDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val touchDataRepository: TouchDataRepository
) : ViewModel() {
    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning: StateFlow<Boolean> get() = _isServerRunning

    private val _replayMode = MutableStateFlow(false)

    private val _touchDataList = MutableStateFlow<List<TouchData>>(emptyList())
    val touchDataList: StateFlow<List<TouchData>> get() = _touchDataList

    var showConfigDialog by mutableStateOf(false)
    var serverPort by mutableStateOf("8082")

    private var server = createServer()

    private fun createServer() = embeddedServer(CIO, port = serverPort.toInt()) {
        install(WebSockets)
        routing {
            webSocket("/track") {
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    println("Received: $receivedText")

                    try {
                        val swipeData = Json.decodeFromString<TouchData>(receivedText)
                        viewModelScope.launch {
                            touchDataRepository.saveTouchData(swipeData)
                            _touchDataList.value = touchDataRepository.getAllTouchData().first()
                        }
                    } catch (e: Exception) {
                        println("Error in WebSocket session: ${e.message}")
                    }
                }
            }
        }
    }

    fun toggleServer() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isServerRunning.value) {
                println("Stopping server...")
                server.stop(1000, 10000)
                _isServerRunning.value = false
                println("Server stopped")
            } else {
                println("Starting server...")
                server = createServer()
                clearData()
                server.start(wait = false)
                _isServerRunning.value = true
                println("Server started on port $serverPort")
            }
        }
    }

    fun openConfigDialog() {
        showConfigDialog = true
    }

    fun closeConfigDialog() {
        showConfigDialog = false
    }

    fun saveConfig() {
        closeConfigDialog()
        if (_isServerRunning.value) {
            toggleServer()
            toggleServer()
        }
    }

    fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            touchDataRepository.clearAllData()
            _touchDataList.value = emptyList()
        }
    }

    suspend fun replayTouchData(onTouchData: (TouchData) -> Unit) {
        _touchDataList.value = emptyList()
        val touchDataList = touchDataRepository.getAllTouchData().first()
        if (touchDataList.isNotEmpty()) {
            var previousTimestamp = touchDataList[0].timestamp
            for (touchData in touchDataList) {
                val delayDuration = touchData.timestamp - previousTimestamp
                kotlinx.coroutines.delay(delayDuration)
                onTouchData(touchData)
                previousTimestamp = touchData.timestamp
            }
        }
        _replayMode.value = false
    }

    fun updateReplayTrack(touchData: TouchData) {
        _touchDataList.value += touchData
    }
}
