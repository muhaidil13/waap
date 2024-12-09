package com.example.wapp.screen

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wapp.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onSplashFinish:() -> Unit) {
    var startAnimation by remember {
        mutableStateOf(false)
    }

    val alpaOpacity by animateFloatAsState(
        targetValue = if (startAnimation) 0.3f else 1f,
        label = "animate",
        animationSpec = tween(
            durationMillis = 3000,
            easing = FastOutLinearInEasing
        )
    )
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000)
        onSplashFinish()
        startAnimation = false
    }
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onPrimary)
            .fillMaxSize()
            .alpha(alpaOpacity), contentAlignment = Alignment.Center
    ) {
        Column {
            Text(text = "Logo Aplikasi", style = TextStyle(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            ))
        }
    }
}