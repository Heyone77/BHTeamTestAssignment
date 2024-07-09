package com.example.testingtaskbhteam.presentation.viewmodels

import io.ktor.client.plugins.plugin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    private val _isStarted = MutableStateFlow(false)
    val isStarted: StateFlow<Boolean> get() = _isStarted

    var showConfigDialog by mutableStateOf(false)
    var serverIp by mutableStateOf("10.0.2.2") // IP адрес хост-машины
    var serverPort by mutableStateOf("8082")  // Новый порт
    var frequencyX by mutableStateOf("10")

    private var session: WebSocketSession? = null

    init {
        client.plugin(WebSockets)
    }

    fun toggleStartStop() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isStarted.value) {
                session?.close()
                _isStarted.value = false
                println("Disconnected from server")
            } else {
                try {
                    println("Attempting to connect to ws://$serverIp:$serverPort")
                    session = client.webSocketSession {
                        url("ws://$serverIp:$serverPort")
                    }
                    _isStarted.value = true
                    println("Connected to server at ws://$serverIp:$serverPort")
                    startSendingData()
                } catch (e: Exception) {
                    println("Error connecting to server: ${e.message}")
                }
            }
        }
    }

    private fun startSendingData() {
        viewModelScope.launch(Dispatchers.IO) {
            session?.let { wsSession ->
                while (_isStarted.value) {
                    val data = generateTouchData()
                    wsSession.send(Frame.Text(data))
                    println("Sent: $data")
                    kotlinx.coroutines.delay(1000L / frequencyX.toLong())
                }
            }
        }
    }

    private fun generateTouchData(): String {
        return "{\"x\":100, \"y\":200, \"pressure\":0.5, \"size\":1.0}"
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            session?.close()
            println("Session closed")
        }
    }
}