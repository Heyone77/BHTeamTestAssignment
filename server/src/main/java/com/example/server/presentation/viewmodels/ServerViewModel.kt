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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val touchDataRepository: TouchDataRepository
) : ViewModel() {
    private val _isServerRunning = MutableStateFlow(false)
    val isServerRunning: StateFlow<Boolean> get() = _isServerRunning

    var showConfigDialog by mutableStateOf(false)
    var serverPort by mutableStateOf("8082")

    val touchDataList: StateFlow<List<TouchData>> = touchDataRepository.getAllTouchData()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var server = createServer()

    private fun createServer() = embeddedServer(CIO, port = serverPort.toInt()) {
        install(WebSockets)
        routing {
            webSocket("/track") {
                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        println("Received: $receivedText")


                        val touchData = Json.decodeFromString<TouchData>(receivedText)
                        viewModelScope.launch {
                            touchDataRepository.saveTouchData(touchData)
                        }

                        send(Frame.Text("Echo: $receivedText"))
                    }
                } catch (e: Exception) {
                    println("Error in WebSocket session: ${e.message}")
                } finally {
                    println("WebSocket session closed")
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
            toggleServer() // Stop the server if it's running
            toggleServer() // Start the server with new configuration
        }
    }

    fun clearData() {
        viewModelScope.launch {
            touchDataRepository.clearAllData()
        }
    }
}