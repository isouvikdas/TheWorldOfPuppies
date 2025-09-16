package com.example.theworldofpuppies.booking.dog_training.presentation

import android.content.Context
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.booking.core.presentation.BookingSuccessDialog
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBookingUIState
import com.example.theworldofpuppies.booking.grooming.presentation.BookingHeader
import com.example.theworldofpuppies.booking.pet_walk.presentation.PriceRowSection
import com.example.theworldofpuppies.booking.vet.presentation.VetBookingBottomSection
import com.example.theworldofpuppies.booking.vet.presentation.VetBookingNameSection
import com.example.theworldofpuppies.booking.vet.presentation.VetBookingViewModel
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.core.presentation.util.calculateDiscountedPrice
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingFeature
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingOption
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingUiState
import com.example.theworldofpuppies.services.dog_training.presentation.DogTrainingViewModel
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetOption
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot
import com.example.theworldofpuppies.services.vet.domain.toString
import com.example.theworldofpuppies.shop.order.presentation.AddressSection
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogTrainingBookingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dogTrainingBookingViewModel: DogTrainingBookingViewModel,
    dogTrainingBookingUiState: DogTrainingBookingUIState,
    dogTrainingUiState: DogTrainingUiState,
    addressUiState: AddressUiState,
    addressViewModel: AddressViewModel
) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val id = dogTrainingUiState.id
    val name = dogTrainingUiState.name
    val discount = dogTrainingUiState.discount
    val dogTrainingOption = dogTrainingUiState.selectedDogTrainingOption
    val dogTrainingFeatures = dogTrainingUiState.selectedDogTrainingFeatures

    val selectedAddress = addressUiState.addresses.find { it.isSelected }

    if (dogTrainingBookingUiState.showSuccessDialog && dogTrainingBookingUiState.dogTrainingBooking != null) {
        BookingSuccessDialog(
            bookingId = dogTrainingBookingUiState.dogTrainingBooking.publicBookingId,
            onBookingView = {
                dogTrainingBookingViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Home.route
                )
                dogTrainingBookingViewModel.resetUiState()
            },
            onDismiss = {
                dogTrainingBookingViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Home.route
                )
                dogTrainingBookingViewModel.resetUiState()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                BookingHeader(scrollBehavior = scrollBehavior, navController = navController)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AddressSection(
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.dimens.extraSmall)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            selectedAddress = selectedAddress,
                            addressViewModel = addressViewModel,
                            onAddressChangeClick = {
                                dogTrainingBookingViewModel.onAddressChangeClick(
                                    navController
                                )
                            },
                            navController = navController,
                            heading = "Service Address",
                            addressList = addressUiState.addresses
                        )
                    }
                }

                DogTrainingBookingBottomSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    discount = discount,
                    name = name,
                    selectedFeatures = dogTrainingFeatures,
                    selectedDogTrainingOption = dogTrainingOption,
                    serviceId = id,
                    dogTrainingBookingViewModel = dogTrainingBookingViewModel,
                    context = context,
                )

            }
        }
        if (dogTrainingBookingUiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(0.2f))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun DogTrainingBookingBottomSection(
    modifier: Modifier = Modifier,
    discount: Int = 0,
    name: String?,
    selectedFeatures: List<DogTrainingFeature>,
    selectedDogTrainingOption: DogTrainingOption? = null,
    serviceId: String?,
    dogTrainingBookingViewModel: DogTrainingBookingViewModel,
    context: Context
) {

    val totalPrice = selectedFeatures.sumOf { calculateDiscountedPrice(discount, it.price) }

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
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

            DogTrainingBookingNameSection(
                name = name,
                dogTrainingOption = selectedDogTrainingOption?.name
            )
            Spacer(modifier = Modifier.height(6.dp))
            selectedFeatures.forEach { feature->
                PriceRowSection(
                    priceTitle = feature.name,
                    price1 = feature.price,
                    price2 = calculateDiscountedPrice(discount = discount, price = feature.price)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            PriceRowSection(
                priceTitle = "",
                price2 = totalPrice
            )
            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    dogTrainingBookingViewModel.createBooking(
                        serviceId = serviceId,
                        petId = "",
                        notes = "",
                        dogTrainingOption = selectedDogTrainingOption,
                        dogTrainingFeatures = selectedFeatures,
                        context = context
                    )
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
                ),
            ) {
                Text(
                    text = "Place Booking",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }
}

@Composable
fun DogTrainingBookingNameSection(
    modifier: Modifier = Modifier,
    name: String? = null,
    dogTrainingOption: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Service",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.W500
        )
        if (!name.isNullOrEmpty() && !dogTrainingOption.isNullOrEmpty()) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "($dogTrainingOption)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }
    }
}


