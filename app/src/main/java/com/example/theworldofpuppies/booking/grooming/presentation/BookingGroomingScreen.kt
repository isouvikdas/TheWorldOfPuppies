package com.example.theworldofpuppies.booking.grooming.presentation

import android.content.Context
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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.core.domain.toString
import com.example.theworldofpuppies.booking.core.presentation.BookingSuccessDialog
import com.example.theworldofpuppies.booking.core.presentation.WalletBalanceRow
import com.example.theworldofpuppies.booking.grooming.domain.GroomingSlot
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.core.presentation.util.calculateDiscountedPrice
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.core.presentation.util.formatDateTime
import com.example.theworldofpuppies.core.presentation.util.formatDayOfMonth
import com.example.theworldofpuppies.core.presentation.util.formatDayOfWeek
import com.example.theworldofpuppies.core.presentation.util.toEpochMillis
import com.example.theworldofpuppies.core.presentation.util.toLocalDateTime
import com.example.theworldofpuppies.membership.domain.PremiumOptionUiState
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.domain.Pet
import com.example.theworldofpuppies.refer_earn.domain.ReferEarnUiState
import com.example.theworldofpuppies.services.core.presentation.component.ServiceTopAppBar
import com.example.theworldofpuppies.services.grooming.domain.GroomingSubService
import com.example.theworldofpuppies.services.grooming.domain.GroomingUiState
import com.example.theworldofpuppies.shop.order.presentation.AddressSection
import com.example.theworldofpuppies.ui.theme.dimens
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingGroomingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addressViewModel: AddressViewModel,
    addressUiState: AddressUiState,
    groomingBookingViewModel: GroomingBookingViewModel,
    groomingUiState: GroomingUiState,
    selectedPetForBooking: Pet?,
    referEarnUiState: ReferEarnUiState,
    premiumOptionUiState: PremiumOptionUiState
) {
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val groomingBookingUiState by groomingBookingViewModel.groomingBookingUiState.collectAsStateWithLifecycle()

    val selectedAddress = addressUiState.addresses.find { it.isSelected }

    val bookingTimeUiState by groomingBookingViewModel.bookingTimeUiState.collectAsStateWithLifecycle()

    var discount = groomingUiState.grooming?.discount
    if (premiumOptionUiState.premiumOptionOrder?.endDate?.isAfter(LocalDateTime.now()) == true) {
        discount = discount?.plus(15)
    }

    val selectedDate = bookingTimeUiState.selectedDate

    var showDateDialog by remember { mutableStateOf(false) }

    val selectedSubService =
        groomingUiState.subServices.find { it.id == groomingUiState.selectedSubServiceId }

    val serviceId =
        groomingUiState.grooming?.id ?: Category.GROOMING.name.toLowerCase(locale = Locale("en-US"))

    LaunchedEffect(Unit) {
        if (bookingTimeUiState.timeSlots.isEmpty()) {
            groomingBookingViewModel.getBookingTimeSlots(context)
        }
    }

    val isLoading = bookingTimeUiState.isLoading
    val filteredTimeSlots = bookingTimeUiState.slotPerDate
    val currentTime = bookingTimeUiState.currentTime

    val isItToday = selectedDate.toLocalDate() == currentTime.toLocalDate()
    val finalTimeSlots = if (isItToday) {
        filteredTimeSlots
            .filter { it.startTime.isAfter(currentTime.plusHours(1)) }
    } else {
        filteredTimeSlots
    }

    val selectedSlot = bookingTimeUiState.selectedSlot

    val dateDisplayPattern = when (selectedDate.toLocalDate()) {
        currentTime.toLocalDate() -> "'Today', dd MMM yyyy"
        currentTime.toLocalDate().plusDays(1) -> "'Tomorrow', dd MMM yyyy"
        currentTime.toLocalDate().minusDays(1) -> "'Yesterday', dd MMM yyyy"
        else -> "EEEE, dd MMM yyyy"
    }

    if (groomingBookingUiState.showBookingSuccessDialog && groomingBookingUiState.booking != null) {
        BookingSuccessDialog(
            bookingId = groomingBookingUiState.booking?.publicBookingId ?: "",
            onBookingView = {
                groomingBookingViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Home.route
                )
                groomingBookingViewModel.resetUiStates()
            },
            onDismiss = {
                groomingBookingViewModel.dismissDialog(
                    navController = navController,
                    route = BottomNavigationItems.Home.route
                )
                groomingBookingViewModel.resetUiStates()
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
            if (showDateDialog) {
                DatePickerModal(
                    onDateSelected = { date ->
                        date?.let { it ->
                            groomingBookingViewModel.onDateSelect(it)
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
                                heading = formatDateTime(
                                    selectedDate,
                                    pattern = dateDisplayPattern
                                ),
                                groomingBookingViewModel = groomingBookingViewModel,
                                onShowDateDialogChange = { showDateDialog = it },
                                timeSlots = finalTimeSlots,
                                selectedDate = selectedDate,
                                error = bookingTimeUiState.errorMessage,
                                isLoading = isLoading,
                                selectedSlot = selectedSlot,
                                onTimeSlotSelection = { slot ->
                                    groomingBookingViewModel.onTimeSlotSelection(
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
                                    groomingBookingViewModel.onAddressChangeClick(
                                        navController
                                    )
                                },
                                navController = navController,
                                heading = "Service Address",
                                addressList = addressUiState.addresses
                            )
                        }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2.times(2))) }

                    }

                    BookingGroomingBottomSection(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .zIndex(1f),
                        subService = selectedSubService,
                        selectedAddress = selectedAddress,
                        selectedSlot = selectedSlot,
                        context = context,
                        groomingBookingViewModel = groomingBookingViewModel,
                        serviceId = serviceId,
                        petId = selectedPetForBooking?.id ?: "",
                        walletBalance = referEarnUiState.walletBalance,
                        discount = discount
                    )
                }
            }
        }

        if (groomingBookingUiState.isLoading) {
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectDateSection(
    modifier: Modifier = Modifier,
    heading: String,
    groomingBookingViewModel: GroomingBookingViewModel,
    onShowDateDialogChange: (Boolean) -> Unit = {},
    timeSlots: List<GroomingSlot>,
    selectedDate: LocalDateTime,
    error: String? = null,
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
                groomingBookingViewModel.onDateSelect(it)
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
                        CircularProgressIndicator(
                            modifier = Modifier.padding(
                                vertical = 50.dp,
                                horizontal = MaterialTheme.dimens.small1
                            )
                        )
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

                            val finalModifier = if (isAvailable) {
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
                                modifier = finalModifier,
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
                                        "${slot.startTime.toLocalTime()} - ${slot.endTime.toLocalTime()}",
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
                            fontWeight = FontWeight.W500,
                            modifier = Modifier.padding(
                                vertical = 50.dp,
                                horizontal = MaterialTheme.dimens.small1
                            )
                        )

                    }

                }
            }
        }
    }
}


@Composable
fun DaySelectorCard(
    modifier: Modifier = Modifier,
    selectedDate: LocalDateTime,
    onDateSelect: (LocalDateTime) -> Unit = {}
) {

    val today = LocalDateTime.now()
    val next7Days = (0..6).map { today.plusDays(it.toLong()) }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(next7Days) { date ->

            val isSelected = date.toLocalDate() == selectedDate.toLocalDate()
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
                        formatDayOfWeek(date.toLocalDate()),
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
                                formatDayOfMonth(date.toLocalDate()),
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
    onDateSelected: (LocalDateTime?) -> Unit,
    onDismiss: () -> Unit,
    selectedDate: LocalDateTime
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
                    ?.toLocalDateTime()
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
fun ServiceSummaryCard(
    modifier: Modifier = Modifier,
    subService: GroomingSubService,
    context: Context,
    walletBalance: Double = 0.0,
    discount: Int?
) {
    val price = subService.price
    val subTotal = calculateDiscountedPrice(discount ?: 0, price)
    val finalWalletBalance = if (walletBalance <= subTotal) {
        walletBalance
    } else {
        subTotal
    }
    val totalPrice = subTotal - finalWalletBalance

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = MaterialTheme.dimens.small1),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Service Session (1)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W500
            )
            Column(verticalArrangement = Arrangement.Top) {
                Text(
                    Category.GROOMING.toString(context),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    "(${subService.name})",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )


            }

        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Service Fee",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W500
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    formatCurrency(price),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.W500,
                    textDecoration = TextDecoration.LineThrough,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray.copy(0.9f),
                    modifier = Modifier.padding(end = 10.dp)
                )

                Text(
                    formatCurrency(subTotal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )

            }
        }
        if (walletBalance > 0) {
            WalletBalanceRow(
                walletBalance = finalWalletBalance
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                "Total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                formatCurrency(totalPrice),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

        }
    }
}

@Composable
fun BookingGroomingBottomSection(
    modifier: Modifier = Modifier,
    discount: Int? = 0,
    subService: GroomingSubService? = null,
    selectedSlot: GroomingSlot? = null,
    selectedAddress: Address? = null,
    groomingBookingViewModel: GroomingBookingViewModel,
    context: Context,
    serviceId: String,
    petId: String,
    walletBalance: Double = 0.0
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
            subService?.let {
                ServiceSummaryCard(
                    subService = subService,
                    context = context,
                    walletBalance = walletBalance,
                    discount = discount
                )
            }
            Button(
                onClick = {
                    groomingBookingViewModel.placeBooking(
                        subService = subService,
                        selectedSlot = selectedSlot,
                        selectedDate = selectedSlot?.startTime?.toLocalDate()?.atStartOfDay(),
                        selectedAddress = selectedAddress,
                        context = context,
                        serviceId = serviceId,
                        petId = petId
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
                )
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
