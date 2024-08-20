package com.example.crowdin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.SwipeProgress
import androidx.wear.compose.material.SwipeableDefaults
import androidx.wear.compose.material.SwipeableState
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SlideToUnlock(
    isLoading: Boolean,
    onUnlockRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val swipeState = rememberSwipeableState(
        initialValue = if (isLoading) Anchor.End else Anchor.Start,
        confirmStateChange = { anchor ->
            if (anchor == Anchor.End) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onUnlockRequested()
            }
            true
        }
    )

    val swipeFraction by remember {
        derivedStateOf {
            try {
                calculateSwipeFraction(swipeState.progress)
            } catch (e: Exception) {
                Log.e("SwipeError", "Error calculating swipe fraction")
                0f
            }
        }
    }


    LaunchedEffect(isLoading) {
        swipeState.animateTo(if (isLoading) Anchor.End else Anchor.Start)
    }

    Track(
        swipeState = swipeState,
        swipeFraction = swipeFraction,
        enabled = !isLoading,
        modifier = modifier,
    ) {
        Hint(
            text = "Swipe to Cancel SOS",
            swipeFraction = swipeFraction,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(PaddingValues(horizontal = Thumb.Size + 8.dp)),
        )

        Thumb(
            isLoading = isLoading,
            modifier = Modifier.offset {
                IntOffset(swipeState.offset.value.roundToInt(), 0)
            },
        )
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
fun calculateSwipeFraction(progress: SwipeProgress<Anchor>): Float {
    if (progress.fraction < 0 || progress.fraction > 1) return 0f
    if (progress.fraction.toString().contains("-")) return 0.001f
    val atAnchor = progress.from == progress.to
    val fromStart = progress.from == Anchor.Start

    return if (atAnchor) {
        if (fromStart) 0f else 1f
    } else {
        val fraction = progress.fraction.coerceIn(0f, 1f)
        if (fromStart) fraction else 1f - fraction
    }
}


enum class Anchor { Start, End }

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun Track(
    swipeState: SwipeableState<Anchor>,
    swipeFraction: Float,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit),
) {
    val density = LocalDensity.current
    var fullWidth by remember { mutableIntStateOf(0) }

    val horizontalPadding = 10.dp

    val startOfTrackPx = 0f
    val endOfTrackPx = remember(fullWidth) {
        with(density) { fullWidth - (2 * horizontalPadding + Thumb.Size).toPx() }
    }

    val snapThreshold = 0.8f
    val thresholds = { from: Anchor, to: Anchor ->
        if (from == Anchor.Start) {
            FractionalThreshold(snapThreshold.coerceIn(0f, 1f)) // Ensure threshold is within bounds
        } else {
            FractionalThreshold((1f - snapThreshold).coerceIn(0f, 1f))
        }
    }

    val backgroundColor by remember(swipeFraction) {
        derivedStateOf { calculateTrackColor(swipeFraction) }
    }

    Box(
        modifier = modifier
            .onSizeChanged { fullWidth = it.width }
            .height(56.dp)
            .fillMaxWidth()
            .swipeable(
                enabled = enabled,
                state = swipeState,
                orientation = Orientation.Horizontal,
                anchors = mapOf(
                    startOfTrackPx to Anchor.Start,
                    endOfTrackPx to Anchor.End,
                ),
                thresholds = thresholds,
                velocityThreshold = Track.VelocityThreshold,
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(percent = 50),
            )
            .border(
                width = 1.dp,
                color = Color.Yellow.copy(alpha = 0.5f),
                shape = RoundedCornerShape(percent = 50),
            )
            .padding(
                PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = 8.dp,
                )
            ),
        content = content,
    )
}

val AlmostBlack = Color(0xFF111111)
val Yellow = Color(0xFF1E1F16)
fun calculateTrackColor(swipeFraction: Float): Color {
    val endOfColorChangeFraction = 0.4f
    val fraction = (swipeFraction / endOfColorChangeFraction).coerceIn(0f..1f)
    return lerp(AlmostBlack, Yellow, fraction)
}

@Composable
fun Thumb(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(Thumb.Size)
            .background(color = Color(0xFFEC2865), shape = CircleShape)
            .padding(8.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(2.dp),
                color = Color.Black,
                strokeWidth = 2.dp
            )
        } else {
            Image(
                painter = painterResource(R.drawable.sos_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Black),
            )
        }
    }
}

@Composable
fun Hint(
    text: String,
    swipeFraction: Float,
    modifier: Modifier = Modifier,
) {
    val hintTextColor by remember(swipeFraction) {
        derivedStateOf { calculateHintTextColor(swipeFraction) }
    }

    Text(
        text = text,
        color = hintTextColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

fun calculateHintTextColor(swipeFraction: Float): Color {
    val endOfFadeFraction = 0.35f
    val fraction = (swipeFraction / endOfFadeFraction).coerceIn(0f..1f)
    return lerp(Color.White, Color.White.copy(alpha = 0f), fraction)
}

private object Thumb {
    val Size = 40.dp
}

private object Track {
    @OptIn(ExperimentalWearMaterialApi::class)
    val VelocityThreshold = SwipeableDefaults.VelocityThreshold * 10
}

@OptIn(ExperimentalWearMaterialApi::class)
@Preview
@Composable
private fun Preview() {
    val previewBackgroundColor = Color(0xFFEDEDED)
    var isLoading by remember { mutableStateOf(false) }
    val spacing = 88.dp
    Column(
        verticalArrangement = spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(previewBackgroundColor)
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(spacing))

        Column(modifier = Modifier.width(IntrinsicSize.Max)) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Normal")
                Spacer(modifier = Modifier.weight(1f))
                Thumb(isLoading = false)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Loading")
                Spacer(modifier = Modifier.widthIn(min = 16.dp))
                Thumb(isLoading = true)
            }


        }

        Spacer(modifier = Modifier.height(spacing))

        Text(text = "Inactive")
        Track(
            swipeState = SwipeableState(Anchor.Start),
            swipeFraction = 0f,
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            content = {},
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Active")
        Track(
            swipeState = SwipeableState(Anchor.Start),
            swipeFraction = 1f,
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            content = {},
        )


        Spacer(modifier = Modifier.height(spacing))


        SlideToUnlock(
            isLoading = isLoading,
            onUnlockRequested = { isLoading = true },
        )
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(),
            shape = RoundedCornerShape(percent = 50),
            onClick = { isLoading = false }) {
            Text(text = "Cancel loading", style = MaterialTheme.typography.labelMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}