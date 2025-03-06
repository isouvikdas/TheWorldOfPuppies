package com.example.theworldofpuppies.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.ui.theme.AppTheme
import kotlinx.coroutines.launch

private data class OtpField(
    val text: String,
    val index: Int,
    val focusRequester: FocusRequester? = null
)

@Composable
fun OtpInputField(
    otp: MutableState<String>, // The current OTP value.
    count: Int, // Number of OTP boxes.
    otpBoxModifier: Modifier = Modifier
        .border(1.pxToDp(), Color.Gray)
        .background(Color.White),
    otpTextType: KeyboardType = KeyboardType.Number,
    textColor: Color,
) {
    val scope = rememberCoroutineScope()

    // Initialize state for each OTP box with its character and optional focus requester.
    val otpFieldsValues = remember {
        (0 until count).mapIndexed { index, i ->
            mutableStateOf(
                OtpField(
                    text = otp.value.getOrNull(i)?.toString() ?: "",
                    index = index,
                    focusRequester = FocusRequester()
                )
            )
        }
    }

    // Update each OTP box's value when the overall OTP value changes, and manage focus.
    LaunchedEffect(key1 = otp.value) {
        for (i in otpFieldsValues.indices) {
            otpFieldsValues[i].value =
                otpFieldsValues[i].value.copy(otp.value.getOrNull(i)?.toString() ?: "")
        }
        // Request focus on the first box if the OTP is blank (e.g., reset).
        if (otp.value.isBlank()) {
            try {
                otpFieldsValues[0].value.focusRequester?.requestFocus()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Create a row of OTP boxes.
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(count) { index ->
            // For each OTP box, manage its value, focus, and what happens on value change.
            OtpBox(
                modifier = otpBoxModifier,
                otpValue = otpFieldsValues[index].value,
                textType = otpTextType,
                textColor = textColor,
                isLastItem = index == count - 1, // Check if this box is the last in the sequence.
                totalBoxCount = count,
                onValueChange = { newValue ->
                    // Handling logic for input changes, including moving focus and updating OTP state.
                    scope.launch {
                        handleOtpInputChange(index, count, newValue, otpFieldsValues, otp)
                    }
                },
                onFocusSet = { focusRequester ->
                    // Save the focus requester for each box to manage focus programmatically.
                    otpFieldsValues[index].value =
                        otpFieldsValues[index].value.copy(focusRequester = focusRequester)
                },
                onNext = {
                    // Attempt to move focus to the next box when the "next" action is triggered.
                    focusNextBox(index, count, otpFieldsValues)
                },
            )
        }
    }
}

private fun handleOtpInputChange(
    index: Int,
    count: Int,
    newValue: String,
    otpFieldsValues: List<MutableState<OtpField>>,
    otp: MutableState<String>
) {
    // Handle input for the current box.
    if (newValue.length <= 1) {
        // Directly set the new value if it's a single character.
        otpFieldsValues[index].value = otpFieldsValues[index].value.copy(text = newValue)
    } else if (newValue.length == 2) {
        // If length of new value is 2, we can guess the user is typing focusing on current box
        // In this case set the unmatched character only
        otpFieldsValues[index].value =
            otpFieldsValues[index].value.copy(text = newValue.lastOrNull()?.toString() ?: "")
    } else if (newValue.isNotEmpty()) {
        // If pasting multiple characters, distribute them across the boxes starting from the current index.
        newValue.forEachIndexed { i, char ->
            if (index + i < count) {
                otpFieldsValues[index + i].value =
                    otpFieldsValues[index + i].value.copy(text = char.toString())
            }
        }
    }

    // Update the overall OTP state.
    var currentOtp = ""
    otpFieldsValues.forEach {
        currentOtp += it.value.text
    }

    try {
        // Logic to manage focus.
        if (newValue.isEmpty() && index > 0) {
            // If clearing a box and it's not the first box, move focus to the previous box.
            otpFieldsValues.getOrNull(index - 1)?.value?.focusRequester?.requestFocus()
        } else if (index < count - 1 && newValue.isNotEmpty()) {
            // If adding a character and not on the last box, move focus to the next box.
            otpFieldsValues.getOrNull(index + 1)?.value?.focusRequester?.requestFocus()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    otp.value = currentOtp
}

private fun focusNextBox(
    index: Int,
    count: Int,
    otpFieldsValues: List<MutableState<OtpField>>
) {
    if (index + 1 < count) {
        // Move focus to the next box if the current one is filled and it's not the last box.
        try {
            otpFieldsValues[index + 1].value.focusRequester?.requestFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


@Composable
private fun OtpBox(
    modifier: Modifier,
    otpValue: OtpField, // Current value of this OTP box.
    textType: KeyboardType = KeyboardType.Number,
    textColor: Color,
    isLastItem: Boolean, // Whether this box is the last in the sequence.
    totalBoxCount: Int, // Total number of OTP boxes for layout calculations.
    onValueChange: (String) -> Unit, // Callback for when the value changes.
    onFocusSet: (FocusRequester) -> Unit, // Callback to set focus requester.
    onNext: () -> Unit, // Callback for handling "next" action, typically moving focus forward.
) {
    val focusManager = LocalFocusManager.current
    val focusRequest = otpValue.focusRequester ?: FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Calculate the size of the box based on screen width and total count.
    // If you're using this in Kotlin multiplatform mobile
    // val screenWidth = LocalWindowInfo.current.containerSize.width
    // If you're using this in Android
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.dpToPx().toInt()
    val paddingValue = 5
    val totalBoxSize = (screenWidth / totalBoxCount) - paddingValue * totalBoxCount

    Box(
        modifier = modifier
            .width(43.dp)
            .height(46.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = TextFieldValue(otpValue.text, TextRange(maxOf(0, otpValue.text.length))),
            onValueChange = {
                // Logic to prevent re-triggering onValueChange when focusing.
                if (!it.text.equals(otpValue)) {
                    onValueChange(it.text)
                }
            },
            // Setup for focus and keyboard behavior.
            modifier = Modifier
                .testTag("otpBox${otpValue.index}")
                .focusRequester(focusRequest)
                .onGloballyPositioned {
                    onFocusSet(focusRequest)
                },
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                textAlign = TextAlign.Center,
                color = textColor
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = textType,
                imeAction = if (isLastItem) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {

                    onNext()
                },
                onDone = {
                    // Hide keyboard and clear focus when done.
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            singleLine = true,
            visualTransformation = getVisualTransformation(textType),
        )
    }
}


@Composable
private fun getVisualTransformation(textType: KeyboardType) =
    if (textType == KeyboardType.NumberPassword || textType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Preview
@Composable
fun OtpInputFieldPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val otpState = remember {
                mutableStateOf("12345")
            }
            OtpInputField(
                otp = otpState,  // Use otpState to track the OTP input
                count = 6,  // Number of OTP boxes
                textColor = Color.Black,
                otpBoxModifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
            )

        }
    }
}