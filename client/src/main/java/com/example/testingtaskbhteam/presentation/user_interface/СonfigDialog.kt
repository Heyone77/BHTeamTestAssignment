package com.example.testingtaskbhteam.presentation.user_interface

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.testingtaskbhteam.presentation.viewmodels.ClientViewModel

@Composable
fun ConfigDialog(viewModel: ClientViewModel) {
    Dialog(onDismissRequest = { viewModel.closeConfigDialog() }) {
        Card(
            modifier = Modifier.padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Настройки сервера",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = viewModel.serverIp,
                    onValueChange = { viewModel.serverIp = it },
                    label = { Text("IP сервера") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.serverPort,
                    onValueChange = { viewModel.serverPort = it },
                    label = { Text("Порт") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.frequencyX,
                    onValueChange = { viewModel.frequencyX = it },
                    label = { Text("Сообщений в секунду") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { viewModel.saveConfig() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}
