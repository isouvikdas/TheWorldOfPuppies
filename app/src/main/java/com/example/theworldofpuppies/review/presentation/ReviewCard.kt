package com.example.theworldofpuppies.review.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.core.presentation.util.formatEpochMillis
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.ui.theme.dimens
import java.time.LocalDateTime

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    stars: Double? = 4.0,
    name: String?,
    review: String?,
    date: LocalDateTime?,
    color: Color = Color.LightGray.copy(0.1f)

) {
    Surface(
        modifier = modifier
            .width(MaterialTheme.dimens.ratingCard),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = color
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(horizontal = MaterialTheme.dimens.small1, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    date?.let {
                        Text(
                            text = formatEpochMillis(it.toEpochMillis()),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
                review?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )

                }
                stars?.let {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End
                    ) {
                        for (i in 1..maxStars) {
                            val isSelected = i <= it //stars
                            val icon =
                                if (isSelected) Icons.Filled.Star else Icons.Default.StarOutline
                            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color.Gray
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconTintColor,
                                modifier = Modifier
                                    .size(24.dp) // star size
                            )
                        }
                    }

                }

            }
        }
    }

}
