package com.example.theworldofpuppies.booking.history.presentation

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.core.domain.toString
import com.example.theworldofpuppies.booking.grooming.domain.enums.PaymentStatus
import com.example.theworldofpuppies.booking.grooming.domain.enums.toString
import com.example.theworldofpuppies.booking.history.domain.BookingHistoryUiState
import com.example.theworldofpuppies.booking.history.presentation.component.DogTrainingBookingItem
import com.example.theworldofpuppies.booking.history.presentation.component.GroomingBookingItem
import com.example.theworldofpuppies.booking.history.presentation.component.PetWalkBookingItem
import com.example.theworldofpuppies.booking.history.presentation.component.VetBookingItem
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(
    modifier: Modifier = Modifier,
    bookingHistoryViewModel: BookingHistoryViewModel,
    bookingHistoryUiState: BookingHistoryUiState
) {
    val context = LocalContext.current
    val serviceList = Category.entries - Category.PET_INSURANCE
    var selectedService by remember { mutableStateOf(Category.GROOMING) }

    val groomingBookings = bookingHistoryUiState.groomingBookings
    val dogTrainingBookings = bookingHistoryUiState.dogTrainingBookings
    val vetBookings = bookingHistoryUiState.vetBookings
    val petWalkBookings = bookingHistoryUiState.petWalkBookings

    val groomingError = bookingHistoryUiState.groomingError
    val vetError = bookingHistoryUiState.vetError
    val dogTrainingError = bookingHistoryUiState.dogTrainingError
    val petWalkError = bookingHistoryUiState.petWalkError

    val groomingLoading = bookingHistoryUiState.groomingLoading
    val dogTrainingLoading = bookingHistoryUiState.dogTrainingLoading
    val vetLoading = bookingHistoryUiState.vetLoading
    val petWalkLoading = bookingHistoryUiState.petWalkLoading

    // Load bookings for selected service (only if not loaded yet)
    LaunchedEffect(selectedService) {
        when (selectedService) {
            Category.GROOMING -> {
                if (!bookingHistoryUiState.groomingLoaded)
                    bookingHistoryViewModel.getGroomingBookings()
            }

            Category.DOG_TRAINING -> {
                if (!bookingHistoryUiState.dogTrainingLoaded)
                    bookingHistoryViewModel.getDogTrainingBookings()
            }

            Category.WALKING -> {
                if (!bookingHistoryUiState.petWalkLoaded)
                    bookingHistoryViewModel.getPetWalkBookings()
            }

            Category.VETERINARY -> {
                if (!bookingHistoryUiState.vetLoaded)
                    bookingHistoryViewModel.getVetBookings()
            }

            else -> {}
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Top horizontal tab selector
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(serviceList) { service ->
                    val isSelected = selectedService == service
                    BookingHistoryTypeCard(
                        modifier = Modifier
                            .padding(
                                start = if (serviceList.first() == service) MaterialTheme.dimens.small1 else 0.dp,
                                end = if (serviceList.last() == service) MaterialTheme.dimens.small1 else 0.dp
                            ),
                        name = service.toString(context),
                        onSelect = { selectedService = service },
                        isSelected = isSelected
                    )
                }
            }

            // Category-specific content
            when (selectedService) {
                Category.DOG_TRAINING -> {
                    if (dogTrainingLoading) {
                        LoaderItem()

                    } else if (dogTrainingBookings.isEmpty() && dogTrainingError != null) {
                        EmptyBookingMessage(message = dogTrainingError.toString(context))

                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(dogTrainingBookings) { booking ->
                                DogTrainingBookingItem(
                                    dogTrainingBooking = booking,
                                    context = context
                                )
                            }
                        }
                    }
                }

                Category.VETERINARY -> {
                    if (vetLoading) {
                        LoaderItem()

                    } else if (vetBookings.isEmpty() && vetError != null) {
                        EmptyBookingMessage(message = vetError.toString(context))

                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(vetBookings) { booking ->
                                VetBookingItem(vetBooking = booking, context = context)
                            }

                        }
                    }
                }

                Category.WALKING -> {
                    if (petWalkLoading) {
                        LoaderItem()
                    } else if (petWalkBookings.isEmpty() && petWalkError != null) {
                        EmptyBookingMessage(message = petWalkError.toString(context))

                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(petWalkBookings) { booking ->
                                PetWalkBookingItem(petWalkBooking = booking, context = context)
                            }
                        }
                    }
                }

                Category.GROOMING -> {
                    if (groomingLoading) {
                        LoaderItem()
                    } else if (groomingBookings.isEmpty() && groomingError != null) {
                        EmptyBookingMessage(message = groomingError.toString(context))
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(groomingBookings) { booking ->
                                GroomingBookingItem(groomingBooking = booking, context = context)
                            }
                        }

                    }
                }

                Category.PET_INSURANCE -> {}
            }
        }


    }
}

@Composable
fun LoaderItem() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EmptyBookingMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.dog_sad),
                contentDescription = "dog",
                modifier = Modifier.size(55.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Center
            )
        }

    }
}


@Composable
fun BookingHistoryTypeCard(
    modifier: Modifier = Modifier,
    name: String? = null,
    isSelected: Boolean = false,
    onSelect: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(40.dp)
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
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else Color.Black
                )

            }
        }
    }

}

@Composable
fun PetDetailsRow(
    modifier: Modifier = Modifier,
    petName: String?,
    breed: String?,
    age: String?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        petName?.let {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    text = "Pet name:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

        }

        breed?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    text = "Pet breed:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = breed,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

        }

        age?.let {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    text = "Pet age:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = "$age months",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }

        }

    }
}

@Composable
fun BookingTotalRow(
    modifier: Modifier = Modifier,
    total: Double,
    paymentStatus: PaymentStatus?,
    context: Context
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.4f),
                text = "Total",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = formatCurrency(total),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            paymentStatus?.let {
                Text(
                    paymentStatus.toString(context),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (paymentStatus) {
                        PaymentStatus.AWAITING_PAYMENT -> Color.Gray
                        PaymentStatus.PAID -> MaterialTheme.colorScheme.primary
                        PaymentStatus.FAILED -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )

            }
        }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Bookings"
    ) {

        Icon(
            painterResource(R.drawable.bag_outline),
            contentDescription = "cart",
            modifier = Modifier
                .size(21.dp)
                .bounceClick {
                    navController.navigate(Screen.CartScreen.route)
                }
        )
    }
}