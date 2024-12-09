package com.example.wapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    modifier: Modifier = Modifier,
    rating: Float,
    maxRating: Int = 5,
    onRatingChange: (Float) -> Unit = {}
) {
    val filledStar =  Icons.Filled.Star
    val outlinedStar = Icons.Outlined.Star

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 1..maxRating) {
            val isFilled = i <= rating
            val icon = if (isFilled) filledStar else outlinedStar
            Icon(
                imageVector = icon,
                contentDescription = "Star $i",
                tint = if (isFilled) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onRatingChange(i.toFloat()) }
            )
        }
    }
}