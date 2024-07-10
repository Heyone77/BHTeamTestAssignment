package com.example.testingtaskbhteam.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testingtaskbhteam.data.TouchData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    private val _isStarted = MutableStateFlow(false)
    val isStarted: StateFlow<Boolean> get() = _isStarted

    var showConfigDialog by mutableStateOf(false)
    var serverIp by mutableStateOf("10.0.2.2")
    var serverPort by mutableStateOf("8082")
    var frequencyX by mutableStateOf("1")

    private var session: WebSocketSession? = null
    private val touchData = MutableStateFlow<TouchData?>(null)

    fun toggleStartStop() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isStarted.value) {
                println("Stopping...")
                session?.close()
                session = null
                _isStarted.value = false
            } else {
                try {
                    println("Starting...")
                    session = client.webSocketSession {
                        url("ws://$serverIp:$serverPort/track")
                    }
                    _isStarted.value = true
                    startSendingData()
                } catch (e: Exception) {
                    println("Error connecting to server: ${e.message}")
                }
            }
        }
    }

    private fun startSendingData() {
        viewModelScope.launch(Dispatchers.IO) {
            while (_isStarted.value) {
                touchData.value?.let { data ->
                    sendTouchData(data)
                    // Сброс данных о касании после отправки
                    touchData.value = null
                }
                delay(1000L / frequencyX.toLong())
            }
        }
    }

    fun updateTouchData(newTouchData: TouchData) {
        touchData.value = newTouchData
    }

    private fun sendTouchData(touchData: TouchData) {

        viewModelScope.launch(Dispatchers.IO) {
            session?.let { wsSession ->
                val data = Json.encodeToString(touchData)
                println("Sending data: $data")
                wsSession.send(Frame.Text(data))
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
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            session?.close()
        }
    }
}