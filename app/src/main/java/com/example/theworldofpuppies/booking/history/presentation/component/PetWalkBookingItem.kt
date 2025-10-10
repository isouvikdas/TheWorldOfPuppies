package com.example.theworldofpuppies.booking.history.presentation.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.data.mappers.toAddress
import com.example.theworldofpuppies.address.presentation.util.getAddressDescription
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.core.domain.toString
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.history.presentation.BookingTotalRow
import com.example.theworldofpuppies.booking.history.presentation.PetDetailsRow
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBooking
import com.example.theworldofpuppies.core.presentation.util.formatEpochMillis
import com.example.theworldofpuppies.core.presentation.util.formatPhoneNumber
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.review.domain.ReviewUiState
import com.example.theworldofpuppies.review.presentation.RatingCard
import com.example.theworldofpuppies.review.presentation.ReviewViewModel
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import com.example.theworldofpuppies.services.pet_walking.domain.enums.toString
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun PetWalkBookingItem(
    modifier: Modifier = Modifier,
    context: Context,
    category: Category = Category.WALKING,
    petWalkBooking: PetWalkBooking,
    reviewViewModel: ReviewViewModel,
    navController: NavController,
    reviewUiState: ReviewUiState
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.White,
        shape = RoundedCornerShape(16.dp)
    ) {
        Surface(
            color = Color.LightGray.copy(0.4f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(MaterialTheme.dimens.small1),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (petWalkBooking.publicBookingId.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                "Booking:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.W500
                            )
                            Text(
                                petWalkBooking.publicBookingId,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500
                            )
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "Placed On: ",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        Text(
                            formatEpochMillis(petWalkBooking.creationDate.toEpochMillis()),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                    }
                }

                Text(
                    petWalkBooking.bookingStatus.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (petWalkBooking.bookingStatus) {
                        BookingStatus.PENDING -> Color.Gray
                        BookingStatus.CONFIRMED -> MaterialTheme.colorScheme.primary
                        BookingStatus.COMPLETED -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.error
                    }
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        category.toString(context),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        "(${petWalkBooking.frequency.toString(context)})",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                    )
                }

                if (petWalkBooking.frequency == Frequency.ONE_TIME) {
                    petWalkBooking.serviceDate?.let {
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                "Start Date: ",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier.fillMaxWidth(0.4f)
                            )
                            Text(
                                formatEpochMillis(it.toEpochMillis()),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500
                            )
                        }

                    }
                } else {
                    petWalkBooking.startDate?.let {
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                "Start Date: ",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier.fillMaxWidth(0.4f)
                            )
                            Text(
                                formatEpochMillis(it.toEpochMillis()),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500
                            )
                        }

                    }
                    petWalkBooking.endDate?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                "End Date: ",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier.fillMaxWidth(0.4f)
                            )
                            Text(
                                formatEpochMillis(petWalkBooking.endDate.toEpochMillis()),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.W500
                            )
                        }

                    }
                }

                val addressDescription = getAddressDescription(petWalkBooking.address.toAddress())
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        modifier = Modifier.size(15.dp),
                        contentDescription = "Location",
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = addressDescription,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = formatPhoneNumber(petWalkBooking.address.contactNumber),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                PetDetailsRow(
                    petName = petWalkBooking.name,
                    breed = petWalkBooking.breed,
                    age = petWalkBooking.age
                )

                BookingTotalRow(
                    total = petWalkBooking.totalPrice,
                    paymentStatus = petWalkBooking.paymentStatus,
                    context = context
                )
                if (!petWalkBooking.isRated && petWalkBooking.bookingStatus == BookingStatus.COMPLETED) {
                    RatingCard(
                        maxStars = 5,
                        stars = reviewUiState.stars,
                        onStarsChange = { stars ->
                        reviewViewModel.resetReviewState()
                        reviewViewModel.onStarsChange(stars)
                        reviewViewModel.setBookingType(targetId = petWalkBooking.id, subType = Category.WALKING)
                        navController.navigate(Screen.ReviewScreen.route)

                        }
                    )

                }
            }

        }
    }

}
