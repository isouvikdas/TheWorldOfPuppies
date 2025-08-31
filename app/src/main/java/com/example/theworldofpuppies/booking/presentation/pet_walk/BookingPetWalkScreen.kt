package com.example.theworldofpuppies.booking.presentation.pet_walk

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.booking.presentation.grooming.BookingHeader
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkingUiState
import com.example.theworldofpuppies.shop.order.presentation.AddressSection
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPetWalkScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    petWalkingUiState: PetWalkingUiState,
    addressUiState: AddressUiState,
    addressViewModel: AddressViewModel,
    bookingPetWalkViewModel: BookingPetWalkViewModel
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val selectedAddress = addressUiState.addresses.find { it.isSelected }

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
                onPlaceBookingClick = {
//                    bookingPetWalkViewModel.onBookNowClick(navController)
                },
                numberOfSessions = 10
            )
        }
    }
}

@Composable
fun BookingPetWalkBottomSection(
    modifier: Modifier = Modifier,
    onPlaceBookingClick: () -> Unit,
    numberOfSessions: Int
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
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))

            PriceRowSection(
                priceTitle = "Per Session :",
                price1 = 600.00,
                price2 = 475.00
            )

            Spacer(modifier = Modifier.height(6.dp))

            PriceRowSection(
                priceTitle = "Total :",
                price2 = numberOfSessions * 475.00
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = {
                    onPlaceBookingClick()
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
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }
}

@Composable
fun PriceRowSection(
    modifier: Modifier = Modifier,
    priceTitle: String,
    price1: Double? = null,
    price2: Double? = null,
    priceTitleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    priceTitleWeight: FontWeight = FontWeight.W500,
    priceStyle: TextStyle = MaterialTheme.typography.titleLarge,
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
            fontWeight = priceTitleWeight
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            price1?.let {
                Text(
                    formatCurrency(it),
                    style = priceTitleStyle,
                    fontWeight = priceTitleWeight,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough,
                    fontStyle = FontStyle.Italic
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
