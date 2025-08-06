package com.example.theworldofpuppies.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theworldofpuppies.auth.presentation.component.PhoneNumberField
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.rejowan.ccpc.Country

@Composable
fun LoginScreen(
    loginUiState: LoginUiState,
    loginViewModel: LoginViewModel? = null,
    onRegisterClick: () -> Unit,
    onVerify: () -> Unit
) {

    val showModalBottomSheet = rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(loginUiState.isOtpSent) {
        if (!loginUiState.isLoading && loginUiState.isOtpSent) {
            showModalBottomSheet.value = !showModalBottomSheet.value
        }
    }

    LaunchedEffect(loginUiState.loginSuccess) {
        if (!loginUiState.isLoading && loginUiState.loginSuccess) {
            onVerify()
        }
    }

    var phoneNumber by rememberSaveable {
        mutableStateOf("")
    }

    var selectedCountry by rememberSaveable {
        mutableStateOf(Country.India)
    }

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
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "Let's sign you in",
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 30.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Welcome Back!",
                    fontWeight = FontWeight.W400,
                    fontSize = 23.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(vertical = 10.dp),
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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
                verticalArrangement = Arrangement.Top
            ) {

                Button(
                    onClick = {
                        loginViewModel?.loginUser(
                            phoneNumber = selectedCountry.countryCode + phoneNumber
                        )

                    },
                    enabled = phoneNumber.length == 10,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .size(50.dp)
                        .bounceClick{},
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
                            text = "Login",
                            fontSize = 17.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Don't have an account, let's ")
                    Text(
                        text = "Register",
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onRegisterClick() }
                    )

                }

            }
        }

    }

    VerifyLoginSheet(
        showModalBottomSheet = showModalBottomSheet,
        loginViewModel = loginViewModel,
        loginUiState = loginUiState,
    )
}

@Preview
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(loginUiState = LoginUiState(), onRegisterClick = {}, onVerify = {})
    }
}