package com.knightshrestha.bookmarks.loading.ui.screen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.knightshrestha.bookmarks.authentication.viewmodel.MainViewModel
import com.knightshrestha.bookmarks.core.helpers.UiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoadingScreen(viewModel: MainViewModel) {
    val infiniteTransition = rememberInfiniteTransition(label = "transition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
            }
        ), label = "angle"
    )


    LaunchedEffect("") {
        viewModel.getToken().collectLatest {
            when (it.isNullOrBlank()) {
                true -> {
                    viewModel.changeUiState(UiState.LOGGED_OUT)
                }
                false -> {
                    viewModel.changeUiState(UiState.LOGGED_IN)
                }
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray), contentAlignment = Alignment.Center
    ) {
        Column {
            CircularProgressIndicator(
                progress = 1f,
                modifier = Modifier
                    .size(80.dp)
                    .rotate(angle)
                    .border(
                        12.dp,
                        brush = Brush.sweepGradient(
                            listOf(
                                Color.White,
                                Color.Blue.copy(alpha = 0.1f),
                                Color.Blue
                            )
                        ),
                        shape = CircleShape
                    ),
                strokeWidth = 1.dp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Please Wait")
        }


    }

}