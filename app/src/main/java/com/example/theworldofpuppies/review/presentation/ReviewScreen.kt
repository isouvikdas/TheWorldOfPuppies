package com.example.theworldofpuppies.review.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    var rating by remember { mutableFloatStateOf(0f) }

    var text by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                ReviewScreenHeader(scrollBehavior = scrollBehavior, navController = navController)
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    RatingSection(
                        rating = rating,
                        onRatingChanged = {
                            rating = it
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ReviewDescriptionFieldSection(
                        value = text,
                        onValueChange = { text = it }
                    )
                }

                ReviewScreenBottomSection(
                    navController = navController,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun ReviewDescriptionFieldSection(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {}
) {
    val maxLength = 250
    Column(modifier = modifier) {
        Text(
            "Please elaborate your experience",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.small1,
                    vertical = 10.dp
                ),
            textAlign = TextAlign.Start
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                focusedContainerColor = Color.LightGray.copy(0.25f),
                unfocusedContainerColor = Color.LightGray.copy(0.25f)
            ),
            textStyle = MaterialTheme.typography.titleSmall,
            supportingText = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${value.length}/$maxLength",
                        color = if (value.length == maxLength)
                            MaterialTheme.colorScheme.tertiary
                        else
                            Color.Black,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )

    }

}

@Composable
fun RatingSection(modifier: Modifier = Modifier, rating: Float, onRatingChanged: (Float) -> Unit) {
    Column(modifier = modifier) {
        Text(
            "How would you wish to rate this order?",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.small1,
                    vertical = 10.dp
                ),
            textAlign = TextAlign.Start
        )
        RatingCard(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.small1),
            maxStars = 5,
            rating = rating,
            onRatingChanged = {
                onRatingChanged(it)
            },
            starSize = 35.dp,
            horizontalArrangement = Arrangement.Start
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreenHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Reviews"
    ) {

        Icon(
            painterResource(R.drawable.bag_outline),
            contentDescription = "Cart",
            modifier = Modifier
                .size(21.dp)
                .bounceClick {
                    navController.navigate(Screen.CartScreen.route)
                }
        )
    }
}

@Composable
fun ReviewScreenBottomSection(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zIndex(1f),
        color = Color.White,
        shape = RoundedCornerShape(
            topStart = MaterialTheme.dimens.small3,
            topEnd = MaterialTheme.dimens.small3
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.LightGray.copy(0.55f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
            Button(
                onClick = {
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.buttonHeight)
                    .padding(horizontal = MaterialTheme.dimens.small1),
                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}
