package com.example.theworldofpuppies.services.dog_training.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.core.presentation.component.ServiceTopAppBar
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingFeature
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingUiState
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogTrainingScreen(
    modifier: Modifier = Modifier,
    dogTrainingViewModel: DogTrainingViewModel,
    dogTrainingUiState: DogTrainingUiState,
    navController: NavController
) {
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val id = dogTrainingUiState.id
    val trainingOptions = dogTrainingUiState.dogTrainingOptions
    val selectedTrainingOption = dogTrainingUiState.selectedDogTrainingOption
    val trainingFeatures = selectedTrainingOption?.dogTrainingFeatures
    val selectedFeatures = dogTrainingUiState.selectedDogTrainingFeatures

    LaunchedEffect(Unit) {
        dogTrainingViewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (id.isEmpty()) {
            dogTrainingViewModel.getDogTraining(context)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                DogTrainingHeader(
                    navController = navController,
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
            if (!dogTrainingUiState.isLoading && id.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(R.drawable.dog_sad),
                        contentDescription = "dog",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        dogTrainingUiState.error ?: "Oops! Something went wrong",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    "Choose the Right Training for Your Dog",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.dimens.small1),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "Certified Trainers to Build Obedience, Confidence & Bonding",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimens.small1),
                                    textAlign = TextAlign.Center
                                )

                            }
                        }

                        item {
                            Spacer(modifier = Modifier.padding(16.dp))
                        }

                        item {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(trainingOptions) { option ->
                                    val isSelected = selectedTrainingOption == option
                                    DogTrainingOptionCard(
                                        modifier = Modifier
                                            .padding(
                                                start = if (trainingOptions.first() == option) MaterialTheme.dimens.small1
                                                else 0.dp,
                                                end = if (trainingOptions.last() == option) MaterialTheme.dimens.small1
                                                else 0.dp
                                            ),
                                        name = option.name,
                                        description = option.description,
                                        onSelect = {
                                            dogTrainingViewModel.onTrainingOptionSelect(option)
                                        },
                                        isSelected = isSelected
                                    )
                                }
                            }
                        }

                        item {
                            DogTrainingFeatureSection(
                                trainingFeatures = trainingFeatures,
                                selectedFeatures = selectedFeatures,
                                onSelect = { feature ->
                                    dogTrainingViewModel.onTrainingFeatureSelect(feature)
                                }
                            )
                        }
                    }

                    DogTrainingBottomSection(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .zIndex(1f),
                        dogTrainingViewModel = dogTrainingViewModel,
                        navController = navController
                    )

                }

            }
        }
        if (dogTrainingUiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(0.5f))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

    }
}

@Composable
fun DogTrainingFeatureSection(
    modifier: Modifier = Modifier,
    trainingFeatures: List<DogTrainingFeature>? = null,
    selectedFeatures: List<DogTrainingFeature>,
    onSelect: (DogTrainingFeature) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1)
            .padding(
                top = 16.dp,
                bottom = 190.dp
            ),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray.copy(0.4f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                trainingFeatures?.forEach { trainingFeature ->
                    val isSelected = selectedFeatures.contains(trainingFeature)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(trainingFeature)
                            }
                            .padding(MaterialTheme.dimens.small1),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                trainingFeature.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                trainingFeature.description,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500,
                                color = Color.Gray
                            )
                        }

                        Icon(
                            if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiaryContainer,
                        )
                    }

                    if (trainingFeatures.last() != trainingFeature) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            thickness = 0.3.dp
                        )
                    }


                }
            }
        }
    }
}

@Composable
fun DogTrainingOptionCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    description: String? = null,
    isSelected: Boolean = false,
    onSelect: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(50.dp)
            .clickable {
                onSelect()
            },
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent,
        border = BorderStroke(
            1.2.dp,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            name?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else Color.Black
                )

            }
            description?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    color = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else Color.Black
                )

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogTrainingHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "Dog Training"
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
fun DogTrainingBottomSection(
    modifier: Modifier = Modifier,
    dogTrainingViewModel: DogTrainingViewModel,
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
                    dogTrainingViewModel.onBookNowClick(navController)
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
                    text = "Book Now",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}
