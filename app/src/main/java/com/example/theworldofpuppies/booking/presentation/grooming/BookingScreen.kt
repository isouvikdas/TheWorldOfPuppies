package com.example.theworldofpuppies.booking.presentation.grooming

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.booking.domain.enums.Category
import com.example.theworldofpuppies.booking.domain.grooming.GroomingSlot
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.core.presentation.util.formatDayOfMonth
import com.example.theworldofpuppies.core.presentation.util.formatDayOfWeek
import com.example.theworldofpuppies.core.presentation.util.formatEpochMillis
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.core.presentation.util.toLocalDate
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.grooming.domain.SubService
import com.example.theworldofpuppies.services.utils.presentation.ServiceTopAppBar
import com.example.theworldofpuppies.shop.order.presentation.AddressSection
import com.example.theworldofpuppies.ui.theme.dimens
import java.time.Instant
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addressViewModel: AddressViewModel,
    addressUiState: AddressUiState,
    bookingViewModel: BookingViewModel
) {
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val selectedAddress = addressUiState.addresses.find { it.isSelected }

    val bookingTimeUiState by bookingViewModel.bookingTimeUiState.collectAsStateWithLifecycle()

    val selectedDate = bookingTimeUiState.selectedDate

    var showDateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        bookingViewModel.getBookingTimeSlots(context)
    }

    val isLoading = bookingTimeUiState.isLoading
    val timeSlots = bookingTimeUiState.timeSlots
    val filteredTimeSlots = timeSlots.find { it.date == selectedDate }
    val currentTime = bookingTimeUiState.currentTime
    Log.i("time", currentTime.toString())
    val finalTimeSlots =
        filteredTimeSlots?.slots?.filter { it.startTime.isAfter(currentTime.plusHours(1)) }
            ?: emptyList()
    finalTimeSlots.forEach {
        Log.i("time", it.startTime.toString())
    }
    val selectedSlot = bookingTimeUiState.selectedSlot



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
        if (showDateDialog) {
            DatePickerModal(
                onDateSelected = { date ->
                    date?.let { it ->
                        bookingViewModel.onDateSelect(it, context)
                    }
                },
                onDismiss = { showDateDialog = false },
                selectedDate = selectedDate,
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Color.Transparent
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        SelectDateSection(
                            modifier = Modifier
                                .padding(
                                    horizontal = MaterialTheme.dimens.small1,
                                    vertical = MaterialTheme.dimens.extraSmall
                                ),
                            heading = formatEpochMillis(
                                Instant.now().toEpochMilli(),
                                pattern = "'Today', dd MMM yyyy"
                            ),
                            bookingViewModel = bookingViewModel,
                            onShowDateDialogChange = { showDateDialog = it },
                            timeSlots = finalTimeSlots,
                            selectedDate = selectedDate,
                            context = context,
                            error = bookingTimeUiState.errorMessage,
                            isLoading = isLoading,
                            selectedSlot = selectedSlot,
                            onTimeSlotSelection = { slot ->
                                bookingViewModel.onTimeSlotSelection(
                                    slot
                                )
                            }
                        )
                    }
                    item {
                        AddressSection(
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.dimens.extraSmall)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            selectedAddress = selectedAddress,
                            addressViewModel = addressViewModel,
                            onAddressChangeClick = {
                                bookingViewModel.onAddressChangeClick(
                                    navController
                                )
                            },
                            navController = navController,
                            heading = "Service Address"
                        )
                    }
                    item { Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2.times(2))) }

                }

                BookingBottomSection(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                )

            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectDateSection(
    modifier: Modifier = Modifier,
    heading: String,
    bookingViewModel: BookingViewModel,
    onShowDateDialogChange: (Boolean) -> Unit = {},
    timeSlots: List<GroomingSlot>,
    selectedDate: LocalDate,
    error: String? = null,
    context: Context,
    isLoading: Boolean,
    selectedSlot: GroomingSlot?,
    onTimeSlotSelection: (GroomingSlot) -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.small1
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = MaterialTheme.dimens.extraSmall),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                heading,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Icon(
                painterResource(R.drawable.calendar),
                contentDescription = "calender",
                modifier = Modifier
                    .size(22.dp)
                    .bounceClick {
                        onShowDateDialogChange(true)
                    }
            )
        }
        DaySelectorCard(
            selectedDate = selectedDate,
            onDateSelect = {
                bookingViewModel.onDateSelect(it, context)
            }
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp)
        ) {
            Surface(color = Color.LightGray.copy(0.4f)) {
                if (isLoading) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(100.dp))
                    }
                } else if (timeSlots.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.small1),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        timeSlots.forEach { slot ->
                            val isSelected = slot == selectedSlot
                            val isAvailable = slot.isAvailable

                            val modifier = if (isAvailable) {
                                Modifier
                                    .width(MaterialTheme.dimens.extraLarge1)
                                    .padding(vertical = 10.dp)
                                    .clickable {
                                        onTimeSlotSelection(slot)
                                    }
                            } else {
                                Modifier
                                    .width(MaterialTheme.dimens.extraLarge1)
                                    .padding(vertical = 10.dp)
                            }
                            val containerColor =
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.White.copy(0.7f)
                            val contentColor = if (isSelected) Color.White else Color.Black
                            val availabilityColor =
                                if (isSelected) Color.White else MaterialTheme.colorScheme.primary

                            Surface(
                                modifier = modifier,
                                color = containerColor,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 5.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "${slot.startTime} - ${slot.endTime}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = contentColor
                                    )
                                    Text(
                                        if (isAvailable) "Available" else "Booked",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = availabilityColor
                                    )
                                }
                            }
                        }

                    }
                } else {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            error ?: "No slots available for the selected date",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.padding(vertical = 100.dp, horizontal = 10.dp)
                        )

                    }

                }
            }
        }
    }
}


@Composable
private fun DaySelectorCard(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onDateSelect: (LocalDate) -> Unit = {}
) {

    val today = LocalDate.now()
    val next7Days = (0..6).map { today.plusDays(it.toLong()) }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(next7Days) { date ->

            val isSelected = date == selectedDate
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        onDateSelect(date)
                    },
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            ) {
                Column(
                    modifier = Modifier
                        .size(height = 70.dp, width = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        formatDayOfWeek(date),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Spacer(modifier = Modifier.height(2.5.dp))
                    Surface(
                        modifier = Modifier
                            .size(24.dp),
                        shape = CircleShape,
                        color = if (isSelected) Color.White else Color.Transparent
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                formatDayOfMonth(date),
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                    }
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit,
    selectedDate: LocalDate
) {
    val initialMillis = selectedDate.toEpochMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selected = datePickerState.selectedDateMillis
                    ?.toLocalDate()
                onDateSelected(selected)
                onDismiss()
            }) {
                Text(
                    "OK",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        colors = DatePickerDefaults.colors(
            disabledDayContentColor = Color.Gray
        )
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ServiceSummaryCard(modifier: Modifier = Modifier, subService: SubService? = null) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(MaterialTheme.dimens.small1),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                Category.GROOMING.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                "(Bath & Basic Grooming)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                formatCurrency(1199.00),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textDecoration = TextDecoration.LineThrough,
                fontStyle = FontStyle.Italic,
                color = Color.Gray.copy(0.9f),
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                formatCurrency(900.00),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

        }
    }
}

@Composable
fun BookingBottomSection(modifier: Modifier = Modifier) {
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
            ServiceSummaryCard()
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
                ),
            ) {
                Text(
                    text = "Pay",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "Booking Summary"
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
