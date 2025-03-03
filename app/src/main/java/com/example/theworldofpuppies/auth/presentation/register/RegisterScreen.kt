package com.example.theworldofpuppies.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theworldofpuppies.auth.presentation.PhoneNumberField
import com.example.theworldofpuppies.auth.presentation.TextTextField
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.rejowan.ccpc.Country

@Composable
fun RegisterScreen(
    registrationUiState: RegistrationUiState,
    registrationViewModel: RegistrationViewModel? = null
) {

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
                Text(
                    text = "Let's Register",
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 25.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Welcome!",
                    fontWeight = FontWeight.W400,
                    fontSize = 23.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(vertical = 15.dp),
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextTextField(
                    value = name,
                    onValueChange = { name = it },
                    textColor = Color.Black,
                    fontSize = 18.sp,
                    isNeeded = true,
                    placeHolder = "Name",
                    hint = "Enter Name",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    isVisible = true
                )
                TextTextField(
                    value = email,
                    onValueChange = { email = it },
                    textColor = Color.Black,
                    fontSize = 18.sp,
                    isNeeded = true,
                    placeHolder = "Email",
                    hint = "Enter Email",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    isVisible = false
                )

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
                    isVisible = false
                )

            }
            Text(
                text = "Do you have a referral code ? (optional)",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 17.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clickable { }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        registrationViewModel?.registerUser(
                            username = name,
                            phoneNumber = selectedCountry.countryCode + phoneNumber,
                            email = email
                        )
                    },
                    enabled = phoneNumber.length == 10 && !registrationUiState.isLoading,
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
                    if (registrationUiState.isLoading) {
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
private fun RegisterScreenPreview() {
    AppTheme {
        RegisterScreen(registrationUiState = RegistrationUiState())
    }
}