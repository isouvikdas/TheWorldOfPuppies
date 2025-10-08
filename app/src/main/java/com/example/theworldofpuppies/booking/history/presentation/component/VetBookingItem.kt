package com.example.theworldofpuppies.booking.history.presentation.component

import android.content.Context
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
import com.example.theworldofpuppies.address.data.mappers.toAddress
import com.example.theworldofpuppies.address.presentation.util.getAddressDescription
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.core.domain.toString
import com.example.theworldofpuppies.booking.grooming.domain.enums.BookingStatus
import com.example.theworldofpuppies.booking.history.presentation.BookingTotalRow
import com.example.theworldofpuppies.booking.history.presentation.PetDetailsRow
import com.example.theworldofpuppies.booking.vet.domain.VetBooking
import com.example.theworldofpuppies.core.presentation.util.formatEpochMillis
import com.example.theworldofpuppies.core.presentation.util.formatPhoneNumber
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.review.presentation.RatingCard
import com.example.theworldofpuppies.services.vet.domain.toString
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun VetBookingItem(
    modifier: Modifier = Modifier,
    category: Category = Category.VETERINARY,
    context: Context,
    vetBooking: VetBooking
) {
    var isExpanded by remember { mutableStateOf(false) }
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
                            vetBooking.publicBookingId,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
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
                            formatEpochMillis(vetBooking.creationDate.toEpochMillis()),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                    }
                }

                Text(
                    vetBooking.bookingStatus.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (vetBooking.bookingStatus) {
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
                        "(${
                            vetBooking.vetBookingSnapshot.vetOption.vetBookingCategory.toString(
                                context
                            )
                        })",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                    )
                }
                vetBooking.serviceDate?.let {
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            "Appointment Date: ",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier.fillMaxWidth(0.4f)
                        )
                        Text(
                            formatEpochMillis(vetBooking.serviceDate.toEpochMillis()),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        "Appointment Time: ",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.fillMaxWidth(0.4f)
                    )
                    Text(
                        "${vetBooking.vetTimeSlot.dateTime.toLocalTime()}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500
                    )
                }

                val addressDescription = getAddressDescription(vetBooking.address.toAddress())
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
                    text = formatPhoneNumber(vetBooking.address.contactNumber),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                PetDetailsRow(
                    petName = vetBooking.name,
                    breed = vetBooking.breed,
                    age = vetBooking.age
                )

                BookingTotalRow(
                    total = vetBooking.totalPrice,
                    paymentStatus = vetBooking.paymentStatus,
                    context = context
                )
                if (!vetBooking.isRated && vetBooking.bookingStatus == BookingStatus.COMPLETED) {
                    RatingCard(
                        maxStars = 5,
                        stars = 5f,
                        onStarsChange = { stars ->
//                        reviewViewModel.resetReviewState()
//                        reviewViewModel.onStarsChange(stars)
//                        reviewViewModel.setOrderType(targetId = orderItem.id)
//                        navController.navigate(Screen.ReviewScreen.route)

                        }
                    )

                }

            }

        }
    }

}
