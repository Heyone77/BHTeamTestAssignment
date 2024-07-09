package com.example.server

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.server.presentation.user_interface.ServerScreen
import com.example.server.presentation.viewmodels.ServerViewModel
import com.example.server.ui.theme.TestingTaskBHTeamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestingTaskBHTeamTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val viewModel: ServerViewModel = hiltViewModel()
                    ServerScreen(viewModel)
                }
            }
        }
    }
}


