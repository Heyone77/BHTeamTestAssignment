package com.example.server.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor() : ViewModel() {
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> get() = _isRunning

    var showConfigDialog by mutableStateOf(false)
    var serverPort by mutableStateOf("8082")  // Новый порт

    private var server: ApplicationEngine? = null

    fun toggleServer() {
        viewModelScope.launch {
            if (_isRunning.value) {
                stopServer()
                _isRunning.value = false
            } else {
                startServer()
                _isRunning.value = true
            }
        }
    }

    private fun startServer() {
        viewModelScope.launch(Dispatchers.IO) {
            server = embeddedServer(CIO, port = serverPort.toInt()) {
                install(WebSockets)
                routing {
                    webSocket("/") {
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            println("Received: $receivedText")
                            outgoing.send(Frame.Text("Echo: $receivedText"))
                        }
                    }
                }
            }
            server?.start(wait = false)
            println("Server started on port $serverPort")
        }
    }

    private fun stopServer() {
        viewModelScope.launch(Dispatchers.IO) {
            server?.stop(1000, 1000)
            println("Server stopped")
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
    }
}