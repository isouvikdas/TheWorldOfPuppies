package com.example.theworldofpuppies.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.iOSGlassBackground(): Modifier = this.then(
    Modifier
        .background(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.3f),
                    Color.White.copy(alpha = 0.1f),
                    Color.Gray.copy(alpha = 0.05f)
                ),
                radius = 800f
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            )
        )
        .border(
            width = 0.5.dp,
            color = Color.White.copy(alpha = 0.3f),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            )
        )
)
