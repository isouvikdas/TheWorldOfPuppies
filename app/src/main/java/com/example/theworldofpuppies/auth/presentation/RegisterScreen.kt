package com.example.theworldofpuppies.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary , // Dark Green
                        contentColor = Color.White // Ensures text is visible
                    ),
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Sample Card", color = Color.White)
                    }
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF002f15),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Sample Card", color = Color.White)
                    }
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer, // Dark Green
                        contentColor = Color.White // Ensures text is visible
                    ),
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Sample Card", color = Color.White)
                    }
                }

            }
        }
    }
}



@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen()
}