package com.example.theworldofpuppies.services.vet.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.booking.grooming.presentation.DatePickerModal
import com.example.theworldofpuppies.booking.grooming.presentation.DaySelectorCard
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.core.presentation.util.formatDateTime
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.utils.presentation.ServiceTopAppBar
import com.example.theworldofpuppies.services.vet.domain.VetOption
import com.example.theworldofpuppies.services.vet.domain.VetTimeSlot
import com.example.theworldofpuppies.services.vet.domain.VetUiState
import com.example.theworldofpuppies.services.vet.domain.getIconRes
import com.example.theworldofpuppies.services.vet.domain.toString
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vetViewModel: VetViewModel,
    vetUiState: VetUiState
) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val id = vetUiState.id

    LaunchedEffect(Unit) {
        vetViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (id.isNullOrEmpty()) {
            vetViewModel.getVet(context = context)
        }
    }

    var showDateDialog by remember { mutableStateOf(false) }

    val vetOptions = vetUiState.vetOptions
    val discount = vetUiState.discount ?: 0
    val selectedVetOption = vetUiState.selectedVetOption
    val description = vetUiState.description ?: ""

    val selectedDate = vetUiState.selectedDate
    val timeSlots = vetUiState.slotsPerDate
    val currentDate = LocalDate.now()

    val isItToday = selectedDate.toLocalDate() == currentDate
    val finalTimeSlots = if (isItToday) {
        timeSlots
            .filter { it.dateTime.toLocalTime().isAfter(LocalTime.now().plusHours(1)) }
    } else {
        timeSlots
    }

    val dateDisplayPattern = when (selectedDate.toLocalDate()) {
        currentDate -> "'Today', dd MMM yyyy"
        currentDate.plusDays(1) -> "'Tomorrow', dd MMM yyyy"
        currentDate.minusDays(1) -> "'Yesterday', dd MMM yyyy"
        else -> "EEEE, dd MMM yyyy"
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                VetHeader(scrollBehavior = scrollBehavior, navController = navController)
            }
        ) {
            if (showDateDialog) {
                DatePickerModal(
                    onDateSelected = { date ->
                        date?.let { it ->
                            vetViewModel.onDateSelect(it)
                        }
                    },
                    onDismiss = { showDateDialog = false },
                    selectedDate = selectedDate,
                )
            } else if (!vetUiState.isLoading && id.isNullOrEmpty()) {
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
                        "Oops! Something went wrong",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
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
                                Text(
                                    description,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.dimens.small1),
                                    textAlign = TextAlign.Center
                                )
                            }

                            item {
                                Text(
                                    "Choose between instant in-call consultation or a home visit for your pet’s comfort",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimens.small1),
                                    textAlign = TextAlign.Center
                                )
                            }

                            item {
                                VetOptionSection(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    vetOptions = vetOptions,
                                    selectedVetOptions = selectedVetOption,
                                    context = context,
                                    onVetOptionSelected = { vetOption ->
                                        vetViewModel.onVetOptionSelect(vetOption)
                                    },
                                    discount = discount
                                )
                            }

                            item {
                                VetDateSelectionSection(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = MaterialTheme.dimens.small1,
                                            vertical = MaterialTheme.dimens.extraSmall
                                        ),
                                    heading = formatDateTime(
                                        selectedDate,
                                        pattern = dateDisplayPattern
                                    ),
                                    onShowDateDialogChange = { showDateDialog = it },
                                    vetViewModel = vetViewModel,
                                    timeSlots = finalTimeSlots,
                                    selectedDate = selectedDate,
                                    error = vetUiState.errorMessage,
                                    isDateSectionLoading = vetUiState.isDateSectionLoading,
                                    selectedSlot = vetUiState.selectedSlot,
                                    onTimeSlotSelection = { slot ->
                                        vetViewModel.onTimeSlotSelection(
                                            slot
                                        )
                                    }
                                )

                            }

                            item {
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraLarge1))
                            }
                        }

                        VetBottomSection(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .zIndex(1f),
                            vetViewModel = vetViewModel,
                            navController = navController
                        )
                    }
                }

            }
        }

        if (vetUiState.isLoading) {
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VetDateSelectionSection(
    modifier: Modifier = Modifier,
    heading: String,
    vetViewModel: VetViewModel,
    onShowDateDialogChange: (Boolean) -> Unit = {},
    timeSlots: List<VetTimeSlot>,
    selectedVetOptions: VetOption? = null,
    selectedDate: LocalDateTime,
    error: String? = null,
    isDateSectionLoading: Boolean,
    selectedSlot: VetTimeSlot?,
    onTimeSlotSelection: (VetTimeSlot) -> Unit = {}
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
                vetViewModel.onDateSelect(it)
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
                if (isDateSectionLoading) {
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
                                        "${slot.dateTime.toLocalTime()}",
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
                        val errorText = if (selectedVetOptions != null) error
                            ?: "No slots available for the selected date"
                        else "Please select a vet option"
                        Text(
                            errorText,
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
fun VetOptionSection(
    modifier: Modifier = Modifier,
    vetOptions: List<VetOption>,
    selectedVetOptions: VetOption? = null,
    context: Context,
    discount: Int,
    onVetOptionSelected: (VetOption) -> Unit
) {
    Column(modifier = modifier.padding(vertical = 10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            vetOptions.forEach { vetOption ->
                val isSelected = vetOption == selectedVetOptions
                val price = vetOption.price
                val discountedPrice = price - (price * discount / 100)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = if (isSelected) 6.dp else 0.dp
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.LightGray.copy(0.5f))
                            .clickable {
                                onVetOptionSelected(vetOption)
                            }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(MaterialTheme.dimens.small1),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                painterResource(vetOption.vetBookingCategory.getIconRes()),
                                contentDescription = null,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    vetOption.vetBookingCategory.toString(context),
                                    style = MaterialTheme.typography.titleSmall,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth(0.6f)) {
                                        Text(
                                            vetOption.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.W500
                                        )
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (discount != 0) {
                                            Text(
                                                formatCurrency(price),
                                                style = MaterialTheme.typography.bodySmall,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = FontWeight.W500,
                                                textDecoration = TextDecoration.LineThrough,
                                                fontStyle = FontStyle.Italic,
                                                color = Color.Gray
                                            )
                                        }
                                        Text(
                                            formatCurrency(discountedPrice),
                                            style = MaterialTheme.typography.titleSmall,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                    }
                                }

                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = MaterialTheme.dimens.small1,
                                    vertical = MaterialTheme.dimens.extraSmall.times(2)
                                ),
                            contentAlignment = Alignment.TopEnd,
                        ) {
                            Icon(
                                if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.align(Alignment.TopEnd)
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
fun VetHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "Vet Care at Your Fingertips"
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
fun VetBottomSection(
    modifier: Modifier = Modifier,
    vetViewModel: VetViewModel,
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
                    vetViewModel.onBookNowClick(navController = navController)
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
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}

