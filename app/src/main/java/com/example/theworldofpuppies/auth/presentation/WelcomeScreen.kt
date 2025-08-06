package com.example.theworldofpuppies.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight(0.35f)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(400.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Pet Grooming - Vet on Call - Dog Walking",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 20.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "We Connect Pet Parents with people who'll treat their Pets like family",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                        .padding(horizontal = 30.dp),
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            onLoginClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(start = 20.dp, end = 5.dp)
                        .size(50.dp)
                        .bounceClick{},
                    shape = RoundedCornerShape(15.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)

                ) {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    onClick = {
                        scope.launch {
                            onRegisterClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 20.dp)
                        .size(50.dp)
                        .bounceClick{},
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text(
                        text = "Register",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }


        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    AppTheme {
        WelcomeScreen(onRegisterClick = {}, onLoginClick = {})
    }
}