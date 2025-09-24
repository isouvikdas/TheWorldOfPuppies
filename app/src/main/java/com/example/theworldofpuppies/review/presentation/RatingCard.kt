package com.example.theworldofpuppies.review.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.core.presentation.animation.bounceClick

@Composable
fun RatingCard(
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    starSize: Dp = 30.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {
    val density = LocalDensity.current.density
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.StarOutline
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color.Gray
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .bounceClick(
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .size(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

@Preview
@Composable
private fun RatingCardPreview() {
    Surface(modifier = Modifier.size(300.dp)) {
        var rating by remember { mutableFloatStateOf(0f) } //default rating will be 1

        RatingCard(
            maxStars = 5,
            rating = rating,
            onRatingChanged = {
                rating = it
            }
        )

    }


}