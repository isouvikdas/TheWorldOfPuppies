package com.example.theworldofpuppies.pet_insurance.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.pet_insurance.domain.PetInsuranceUiState
import com.example.theworldofpuppies.pet_insurance.domain.enums.toString
import com.example.theworldofpuppies.profile.pet.presentation.BreedSearchingScreen
import com.example.theworldofpuppies.profile.pet.presentation.PetBreedField
import com.example.theworldofpuppies.profile.pet.presentation.ProfileScreenField
import com.example.theworldofpuppies.services.core.presentation.component.ServiceTopAppBar
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInsuranceScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    petInsuranceViewModel: PetInsuranceViewModel,
    petInsuranceUiState: PetInsuranceUiState
) {
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val features = listOf(
        "Plans starting at Rs 2 per day",
        "Use any vet, anywhere",
        "More than a million pets protected"
    )

    val name = petInsuranceUiState.name ?: ""
    val breed = petInsuranceUiState.breed
    val age = petInsuranceUiState.age ?: ""
    val email = petInsuranceUiState.email ?: ""
    val petType = petInsuranceUiState.petType
    val contactNumber = petInsuranceUiState.contactNumber ?: ""

    val description = "The Best Pet Insurance for a Lifetime of Care"
    val showModalBottomSheet = petInsuranceUiState.showModalBottomSheet

    //errors
    val breedError = petInsuranceUiState.breedError
    val nameError = petInsuranceUiState.nameError
    val ageError = petInsuranceUiState.ageError
    val contactNumberError = petInsuranceUiState.contactNumberError
    val emailError = petInsuranceUiState.emailError

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                PetInsuranceHeader(scrollBehavior = scrollBehavior, navController = navController)
            }
        ) {
            BreedSheetPetInsurance(
                petInsuranceViewModel = petInsuranceViewModel,
                showModalBottomSheet = showModalBottomSheet
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            description,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.small1)
                                .padding(top = 6.dp, bottom = 10.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    item {
                        features.forEach { feature ->
                            PetInsuranceFeatureItem(
                                feature = feature
                            )
                        }
                    }

                    item {
                        Text(
                            "Fill in your details below to receive policy plan quotations",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.small1)
                                .padding(top = 10.dp),
                            textAlign = TextAlign.Start
                        )
                    }

                    item {
                        ProfileScreenField(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.small1,
                            ),
                            heading = "Pet Type?",
                            value = petType.toString(context),
                            onValueChange = { petInsuranceViewModel.onNameChange(it) },
                            errorMessage = nameError,
                            readOnly = true
                        )
                    }

                    item {
                        PetBreedField(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.small1,
                            ),
                            heading = "Pet Breed?",
                            value = breed?.breedName.orEmpty(),
                            readOnly = true,
                            onToggleClick = { petInsuranceViewModel.toggleModalBottomSheet() },
                            errorMessage = breedError
                        )
                    }

                    item {
                        ProfileScreenField(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.small1,
                            ),
                            heading = "Your Name",
                            value = name,
                            onValueChange = { petInsuranceViewModel.onNameChange(it) },
                            errorMessage = nameError
                        )
                    }

                    item {
                        ProfileScreenField(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.small1,
                            ),
                            heading = "Your Email",
                            value = email,
                            onValueChange = { petInsuranceViewModel.onEmailChange(it) },
                            errorMessage = emailError
                        )
                    }

                    item {
                        ProfileScreenField(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.small1,
                            ),
                            heading = "Your Contact Number",
                            value = contactNumber,
                            onValueChange = { petInsuranceViewModel.onContactNumberChange(it) },
                            errorMessage = contactNumberError
                        )
                    }

                    item {
                        ProfileScreenField(
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.small1,
                            ),
                            heading = "Your Pet's age in months",
                            value = age,
                            onValueChange = { petInsuranceViewModel.onAgeChange(it) },
                            errorMessage = ageError,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(180.dp))
                    }

                }
                PetInsuranceBottomSection(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f),
                    petInsuranceViewModel = petInsuranceViewModel,
                    navController = navController
                )

            }
        }
    }
}

@Composable
fun PetInsuranceFeatureItem(modifier: Modifier = Modifier, feature: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        Text(
            feature,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.W500
        )
    }
}

@Composable
fun PetInsuranceBottomSection(
    modifier: Modifier = Modifier,
    petInsuranceViewModel: PetInsuranceViewModel,
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
                    petInsuranceViewModel.onBookNowClick(navController = navController)
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
                    text = "Get Pet Insurance Quotes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInsuranceHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "Pet Insurance"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedSheetPetInsurance(
    modifier: Modifier = Modifier,
    petInsuranceViewModel: PetInsuranceViewModel,
    showModalBottomSheet: Boolean
) {

    val searchUiState by petInsuranceViewModel.searchUiState.collectAsStateWithLifecycle()
    val searchQuery = searchUiState.query
    val breedList by remember(searchUiState.results) {
        derivedStateOf {
            searchUiState.results.sortedBy { it.name.lowercase() }
        }
    }

    var skipPartially by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartially)

    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                petInsuranceViewModel.toggleModalBottomSheet()
            },
            sheetState = bottomSheetState,
            containerColor = Color.White
        ) {
            BreedSearchingScreen(
                searchQuery = searchQuery,
                onSearchTextChange = { petInsuranceViewModel.onSearchTextChange(it) },
                breedList = breedList,
                onBreedSelect = { petInsuranceViewModel.selectBreed(it) }
            )
        }
    }

}
