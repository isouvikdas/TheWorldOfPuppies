package com.example.theworldofpuppies.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theworldofpuppies.auth.presentation.component.PhoneNumberField
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.rejowan.ccpc.Country

@Composable
fun LoginScreen(
    loginUiState: LoginUiState,
    loginViewModel: LoginViewModel? = null
) {

    val showModalBottomSheet = rememberSaveable { mutableStateOf(false) }

    if (!loginUiState.isLoading && loginUiState.isOtpSent) {
        showModalBottomSheet.value = !showModalBottomSheet.value
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
        gmailPattern.matches(email) && !loginUiState.isLoading
    }

    VerifyLoginSheet(
        showModalBottomSheet = showModalBottomSheet,
        loginViewModel = loginViewModel,
        loginUiState = loginUiState
    )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.18f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                PhoneNumberField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    placeHolder = "Phone Number",
                    fontSize = 18.sp,
                    isNeeded = true,
                    textColor = Color.Black,
                    country = selectedCountry,
                    number = phoneNumber,
                    onNumberChange = { phoneNumber = it },
                    onCountryChange = { selectedCountry = it },
                    isVisible = phoneNumber.isNotEmpty()
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        loginViewModel?.loginUser(
                            phoneNumber = selectedCountry.countryCode + phoneNumber
                        )

                    },
                    enabled = phoneNumber.length == 10
                            && isValidEmail && name.length >= 2,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .size(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    if (loginUiState.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Register",
                            fontSize = 17.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

            }
        }

    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(loginUiState = LoginUiState())
    }
}