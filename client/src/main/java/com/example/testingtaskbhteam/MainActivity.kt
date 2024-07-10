package com.example.testingtaskbhteam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testingtaskbhteam.presentation.user_interface.ClientScreen
import com.example.testingtaskbhteam.presentation.viewmodels.ClientViewModel
import com.example.testingtaskbhteam.ui.theme.TestingTaskBHTeamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestingTaskBHTeamTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val viewModel: ClientViewModel = hiltViewModel()
                    ClientScreen(viewModel = viewModel)
                }
            }
        }
    }
}



