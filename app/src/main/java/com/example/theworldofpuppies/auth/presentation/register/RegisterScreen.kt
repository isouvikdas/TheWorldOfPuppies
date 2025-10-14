package com.example.theworldofpuppies.auth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.auth.presentation.component.PhoneNumberField
import com.example.theworldofpuppies.auth.presentation.component.TextTextField
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.example.theworldofpuppies.ui.theme.dimens
import com.rejowan.ccpc.Country

@Composable
fun RegisterScreen(
    registrationUiState: RegistrationUiState,
    registrationViewModel: RegistrationViewModel? = null,
    onVerify: () -> Unit
) {

    val showModalBottomSheet = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(registrationUiState.isOtpSent) {
        if (!registrationUiState.isLoading && registrationUiState.isOtpSent) {
            showModalBottomSheet.value = !showModalBottomSheet.value
        }

    }

    LaunchedEffect(registrationUiState.registrationSuccess) {
        if (!registrationUiState.isLoading && registrationUiState.registrationSuccess) {
            onVerify()
        }
    }

    var phoneNumber by rememberSaveable {
        mutableStateOf("")
    }

    var selectedCountry by rememberSaveable {
        mutableStateOf(Country.India)
    }

    var name by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    val isValidEmail = remember(email) {
        val gmailPattern = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com\$")
        gmailPattern.matches(email) && !registrationUiState.isLoading
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 180.dp)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome Back!",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Let's Register",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                    }

                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
                        TextTextField(
                            value = name,
                            onValueChange = { name = it },
                            isNeeded = true,
                            placeHolder = "Name",
                            hint = "Enter Name",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            isVisible = name.isNotEmpty()
                        )
                        TextTextField(
                            value = email,
                            onValueChange = { email = it },
                            isNeeded = true,
                            placeHolder = "Email",
                            hint = "Enter Email",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            isVisible = email.isNotEmpty()
                        )
                        PhoneNumberField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            placeHolder = "Phone Number",
                            isNeeded = true,
                            textColor = Color.Black,
                            country = selectedCountry,
                            number = phoneNumber,
                            onValueChange = { _, value, _ -> phoneNumber = value },
                            onCountryChange = { selectedCountry = it },
                            isVisible = phoneNumber.isNotEmpty(),
                            keyBoardType = KeyboardType.Number,
                            countryList = Country.entries,
                        )
                        TextTextField(
                            value = name,
                            onValueChange = { name = it },
                            isNeeded = false,
                            placeHolder = "Do you have a referral code? (Optional)",
                            hint = "Code",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            isVisible = name.isNotEmpty(),
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }
//
//            item {
//                Spacer(modifier = Modifier.height(100.dp))
//            }
            }

            Button(
                onClick = {
                    registrationViewModel?.registerUser(
                        username = name,
                        phoneNumber = selectedCountry.countryCode + phoneNumber,
                        email = email
                    )
                },
                enabled = phoneNumber.length == 10
                        && isValidEmail && name.length >= 2,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 20.dp, horizontal = MaterialTheme.dimens.small1)
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.buttonHeight)
                    .bounceClick {},
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                if (registrationUiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }


        }


    }

    if (showModalBottomSheet.value) {
        VerifyRegSheet(
            showModalBottomSheet = showModalBottomSheet,
            registrationUiState = registrationUiState,
            registrationViewModel = registrationViewModel
        )

    }
}


@Preview
@Composable
private fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreen(registrationUiState = RegistrationUiState(), onVerify = {})
    }
}