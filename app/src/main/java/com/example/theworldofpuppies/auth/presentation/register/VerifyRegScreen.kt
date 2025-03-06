package com.example.theworldofpuppies.auth.presentation.register

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theworldofpuppies.auth.presentation.component.OtpInputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyRegSheet(
    showModalBottomSheet: MutableState<Boolean>,
    registrationUiState: RegistrationUiState,
    registrationViewModel: RegistrationViewModel? = null
) {

    val scope = rememberCoroutineScope()
    var skipPartially by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartially)

    if (showModalBottomSheet.value)
        ModalBottomSheet(
            onDismissRequest = {
                showModalBottomSheet.value = false
                registrationViewModel?.toggleOtpSentState()
            },
            sheetState = bottomSheetState,
            containerColor = Color.White
        ) {
            VerifyRegScreen(
                modifier = Modifier
                    .fillMaxSize(),
                registrationUiState = registrationUiState,
                registrationViewModel = registrationViewModel,
                scope = scope,
                bottomSheetState = bottomSheetState,
                showModalBottomSheet = showModalBottomSheet
            )

        }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyRegScreen(
    modifier: Modifier = Modifier,
    registrationUiState: RegistrationUiState,
    registrationViewModel: RegistrationViewModel? = null,
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    showModalBottomSheet: MutableState<Boolean>
) {
    val otp = rememberSaveable { mutableStateOf(registrationUiState.otp) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showModalBottomSheet.value = false
                        }
                    }
                    registrationViewModel?.toggleOtpSentState()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Text(
                    text = "OTP Verification",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )

                IconButton(onClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showModalBottomSheet.value = false
                        }
                    }
                    registrationViewModel?.toggleOtpSentState()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                }

            }


            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = "Please enter the 6-digit code sent to your number.",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 20.dp),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.padding(vertical = 10.dp))


            OtpInputField(
                otp = otp,
                count = 6,
                textColor = Color.Black,
                otpBoxModifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "By proceeding, you agree to our Terms and Conditions and Privacy Policy.",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                )
            }

            Button(
                onClick = {
                    registrationViewModel?.verifyRegistration(
                        phoneNumber = registrationUiState.phoneNumber,
                        otp = otp.value
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                enabled = otp.value.length == 6 && !registrationUiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                if (registrationUiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Verify",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(1.dp))

            registrationUiState.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }


        }
    }
}


//@Preview
//@Composable
//private fun VerifyRegScreenPreview() {
//    AppTheme {
//        VerifyRegScreen(
//            registrationUiState = RegistrationUiState(),
//            onDismiss = {}
//        )
//    }
//}