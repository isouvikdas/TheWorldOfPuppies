package com.example.theworldofpuppies.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.format.TextStyle

@Composable
fun CustomToast(
    message: String,
    durationMillis: Long = 3000,
    onDismiss: () -> Unit
) {
    // Tracks if the toast is currently visible
    var isVisible by remember { mutableStateOf(true) }


   // LaunchedEffect starts a coroutine when this composable first enters the composition.
    // It waits for the specified duration, then hides the toast if it's still visible.
    LaunchedEffect(Unit) {
        delay(durationMillis)
        if (isVisible) {
            isVisible = false // Hide the toast
            onDismiss() // Call the onDismiss callback to let the parent know the toast is gone.
        }
    }

// AnimatedVisibility controls the appearance/disappearance of the toast using animations.
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(), // Fade in animation when the toast appears.
        exit = fadeOut() // Fade out animation when it disappears.
    ) {
        // Box to position the toast at the top center of the screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            // Surface provides a background with dark green color, rounded corners, and elevation
            Surface(
                color = Color(0xFF006400), // Dark green background
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 8.dp
            ) {
                // Row so that the text and the close icon are aligned horizontally
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconButton(
                        onClick = {
                            isVisible = false
                            onDismiss()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                    // The text message takes up as much space as possible
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}