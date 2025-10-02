package com.example.theworldofpuppies.booking.pet_walk.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.booking.core.presentation.BookingSuccessDialog
import com.example.theworldofpuppies.booking.grooming.presentation.BookingHeader
import com.example.theworldofpuppies.booking.pet_walk.presentation.util.calculateSession
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.profile.pet.domain.Pet
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkDateRange
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkingUiState
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import com.example.theworldofpuppies.shop.order.presentation.AddressSection
import com.example.theworldofpuppies.ui.theme.dimens
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPetWalkScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    petWalkingUiState: PetWalkingUiState,
    addressUiState: AddressUiState,
    addressViewModel: AddressViewModel,
    bookingPetWalkViewModel: BookingPetWalkViewModel,
    selectedPetForBooking: Pet?
) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val selectedAddress = addressUiState.addresses.find { it.isSelected }

    val id = petWalkingUiState.id
    val price = petWalkingUiState.basePrice
    val discount = petWalkingUiState.discount
    val name = petWalkingUiState.name
    val startDate = petWalkingUiState.dateRange?.startDate
    val endDate = petWalkingUiState.dateRange?.endDate
    val frequency = petWalkingUiState.selectedFrequency
    val selectedDays = petWalkingUiState.selectedDays
    val singleDate = petWalkingUiState.singleDate

    val petWalkBookingUiState by bookingPetWalkViewModel.petWalkBookingUiState.collectAsStateWithLifecycle()

    if (petWalkBookingUiState.showSuccessDialog && petWalkBookingUiState.petWalkBooking != null) {
        BookingSuccessDialog(
            bookingId = petWalkBookingUiState.petWalkBooking?.publicBookingId ?: "",
            onBookingView = {
                bookingPetWalkViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Home.route
                )
                bookingPetWalkViewModel.resetUiState()
            },
            onDismiss = {
                bookingPetWalkViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Home.route
                )
                bookingPetWalkViewModel.resetUiState()
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
                                bookingPetWalkViewModel.onAddressChangeClick(
                                    navController
                                )
                            },
                            navController = navController,
                            heading = "Service Address",
                            addressList = addressUiState.addresses
                        )
                    }
                }

                BookingPetWalkBottomSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    basePrice = price ?: 0.0,
                    discount = discount ?: 0,
                    name = name,
                    startDate = startDate,
                    endDate = endDate,
                    selectedFrequency = frequency,
                    selectedDays = selectedDays,
                    serviceId = id ?: "",
                    serviceDate = singleDate,
                    bookingPetWalkViewModel = bookingPetWalkViewModel,
                    context = context,
                    petId = selectedPetForBooking?.id ?: ""
                )
            }
        }
        if (petWalkBookingUiState.isLoading) {
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
fun BookingPetWalkBottomSection(
    modifier: Modifier = Modifier,
    basePrice: Double = 0.0,
    discount: Int = 0,
    name: String?,
    startDate: LocalDateTime? = null,
    endDate: LocalDateTime? = null,
    selectedFrequency: Frequency? = null,
    selectedDays: List<Days>,
    serviceId: String,
    serviceDate: LocalDateTime? = null,
    bookingPetWalkViewModel: BookingPetWalkViewModel,
    context: Context,
    petId: String
) {

    val discountedPrice = basePrice.times(100 - discount).div(100)

    var sessionCount = 1

    if (selectedFrequency == Frequency.REPEAT_WEEKLY) {
        if (startDate != null && endDate != null) {
            sessionCount = calculateSession(
                selectedDays = selectedDays,
                startDate = startDate,
                endDate = endDate
            )
        }
    }
    val totalPrice = discountedPrice.times(sessionCount)

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

            PetWalkNameSection(
                name = name,
            )
            Spacer(modifier = Modifier.height(6.dp))
            PriceRowSection(
                priceTitle = "Sessions (1)",
                price1 = basePrice,
                price2 = discountedPrice
            )
            Spacer(modifier = Modifier.height(6.dp))
            PriceRowSection(
                priceTitle = "Sessions ($sessionCount)",
                price2 = totalPrice
            )
            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    selectedFrequency?.let {
                        bookingPetWalkViewModel.createBooking(
                            serviceId = serviceId,
                            serviceDate = serviceDate,
                            dateRange = PetWalkDateRange(
                                startDate = startDate,
                                endDate = endDate
                            ),
                            frequency = it,
                            context = context,
                            selectedDays = selectedDays,
                            petId = petId
                        )

                    }
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
fun PetWalkNameSection(modifier: Modifier = Modifier, name: String? = null) {
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
        name?.let {
            Text(
                it,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun PriceRowSection(
    modifier: Modifier = Modifier,
    priceTitle: String,
    price1: Double? = null,
    price2: Double? = null,
    priceTitleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    priceTitleWeight: FontWeight = FontWeight.W500,
    priceStyle: TextStyle = MaterialTheme.typography.titleMedium,
    priceWeight: FontWeight = FontWeight.SemiBold
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.dimens.small1,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            priceTitle,
            style = priceTitleStyle,
            fontWeight = priceTitleWeight,
            modifier = Modifier.fillMaxWidth(0.5f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (price1 != null) {
                Text(
                    formatCurrency(price1),
                    style = priceTitleStyle,
                    fontWeight = priceTitleWeight,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough,
                    fontStyle = FontStyle.Italic
                )
            } else {
                Text(
                    "Total",
                    style = priceStyle,
                    fontWeight = priceTitleWeight
                )
            }

            price2?.let {
                Text(
                    formatCurrency(it),
                    style = priceStyle,
                    fontWeight = priceWeight
                )

            }
        }
    }

}
