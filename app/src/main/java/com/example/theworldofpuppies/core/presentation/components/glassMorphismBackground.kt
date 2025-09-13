package com.example.theworldofpuppies.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.glassMorphismBackground(): Modifier = this.then(
    Modifier
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.25f),
                    Color.White.copy(alpha = 0.15f)
                )
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            )
        )
        .border(
            width = 1.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.4f),
                    Color.White.copy(alpha = 0.2f)
                )
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp
            )
        )
)
