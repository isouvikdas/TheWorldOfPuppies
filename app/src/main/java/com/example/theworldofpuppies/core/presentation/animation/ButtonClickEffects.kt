package com.example.theworldofpuppies.core.presentation.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


/*========================================*/
//@Composable
//fun NoRippleEffect1() {
//    Button(
//        onClick = {
//        },
//        interactionSource = remember { NoRippleInteractionSource() },
//        shape = RoundedCornerShape(12.dp),
//        contentPadding = PaddingValues(16.dp),
//    ) {
//        Text(text = "Button without Ripple Effect")
//    }
//}

//@Composable
//fun NoRippleEffect2() {
//    Box(
//        modifier = Modifier
//            .height(height = 38.dp)
//            .background(
//                color = Color.Blue,
//                shape = RoundedCornerShape(percent = 12)
//            )
//            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null
//            ) {
//            }
//            .padding(horizontal = 20.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Click profile",
//            color = Color.White
//        )
//    }
//
//}

//@Composable
//fun NoRippleEffect3() {
//    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
//        Button(
//            onClick = {
//                // Click
//            }, shape = RoundedCornerShape(12.dp),
//            contentPadding = PaddingValues(16.dp)
//        ) {
//            Text(text = "Click profile")
//        }
//    }
//}

// Way 3
//private object NoRippleTheme : RippleTheme {
//    @Composable
//    override fun defaultColor() = Color.Unspecified
//
//    @Composable
//    override fun rippleAlpha(): RippleAlpha = RippleAlpha(
//        draggedAlpha = 0.0f,
//        focusedAlpha = 0.0f,
//        hoveredAlpha = 0.0f,
//        pressedAlpha = 0.0f
//    )
//}

// Way 1
//class NoRippleInteractionSource : MutableInteractionSource {
//
//    override val interactions: Flow<Interaction> = emptyFlow()
//
//    override suspend fun emit(interaction: Interaction) {}
//
//    override fun tryEmit(interaction: Interaction) = true
//}

/*================= 2. BOUNCE TOUCH EFFECT =======================*/

@Composable
fun PulsateEffect() {
    Button(
        onClick = {
        }, shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.bounceClick()
    ) {
        Text(text = "Click profile")
    }
}

fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.70f else 1f)

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

enum class ButtonState { Pressed, Idle }

/*====================== 3. AnimatedShapeTouch ============================*/

//@Composable
//fun AnimatedShapeTouch() {
//    val interactionSource = remember { MutableInteractionSource() }
//    val isPressed = interactionSource.collectIsPressedAsState()
//    val cornerRadius by animateDpAsState(targetValue = if (isPressed.value) 10.dp else 50.dp)
//
//    Box(
//        modifier = Modifier
//            .background(color = Color.Blue, RoundedCornerShape(cornerRadius))
//            .size(100.dp)
//            .clip(RoundedCornerShape(cornerRadius))
//            .clickable(
//                interactionSource = interactionSource,
//                indication = rememberRipple()
//            ) {
//            }
//            .padding(horizontal = 20.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Click!",
//            color = Color.White
//        )
//    }
//
//
//}

/*====================== 4. Jump on Touch ============================*/
@Composable
fun PressEffect() {
    Button(
        onClick = {
        }, shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(16.dp),
        modifier = Modifier.pressClickEffect()
    ) {
        Text(text = "Click profile")
    }
}

fun Modifier.pressClickEffect() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val ty by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0f else -20f)

    this
        .graphicsLayer {
            translationY = ty
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}


/*====================== 5. Shake Touch ============================*/

@Composable
fun ShakeEffect() {
    Button(
        onClick = {
        }, shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(16.dp),
        modifier = Modifier.shakeClickEffect()
    ) {
        Text(text = "Click profile")
    }
}

fun Modifier.shakeClickEffect() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val tx by animateFloatAsState(
        targetValue = if (buttonState == ButtonState.Pressed) 0f else -50f,
        animationSpec = repeatable(
            iterations = 2,
            animation = tween(durationMillis = 50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    this
        .graphicsLayer {
            translationX = tx
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}