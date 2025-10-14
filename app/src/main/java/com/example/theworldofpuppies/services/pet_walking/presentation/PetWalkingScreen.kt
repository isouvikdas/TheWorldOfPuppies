package com.example.theworldofpuppies.services.pet_walking.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.grooming.presentation.DatePickerModal
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatDateTime
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.review.domain.ReviewListState
import com.example.theworldofpuppies.review.presentation.ReviewCard
import com.example.theworldofpuppies.review.presentation.ReviewViewModel
import com.example.theworldofpuppies.review.presentation.utils.ReviewEvent
import com.example.theworldofpuppies.review.presentation.utils.ReviewEventManager
import com.example.theworldofpuppies.services.core.presentation.component.ServiceTopAppBar
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkingUiState
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Days
import com.example.theworldofpuppies.services.pet_walking.domain.enums.Frequency
import com.example.theworldofpuppies.services.pet_walking.domain.enums.getIconRes
import com.example.theworldofpuppies.services.pet_walking.domain.enums.toString
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetWalkingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    petWalkingViewModel: PetWalkingViewModel,
    petWalkingUiState: PetWalkingUiState,
    changePetSelectionView: (Boolean) -> Unit,
    reviewViewModel: ReviewViewModel,
    reviewListState: ReviewListState
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val context = LocalContext.current

    val frequencies = petWalkingUiState.frequencies

    val selectedFrequency = petWalkingUiState.selectedFrequency

    val selectedDays = petWalkingUiState.selectedDays

    val days = petWalkingUiState.days

    val description = petWalkingUiState.description

    val discount = petWalkingUiState.discount
    val isRated = petWalkingUiState.isRated
    val totalReviews = petWalkingUiState.totalReviews

    val reviews = reviewListState.petWalkReviews

    LaunchedEffect(isRated) {
        if (isRated) {
            reviewViewModel.getBookingReviews(Category.WALKING)
        }
    }

    LaunchedEffect(Unit) {
        petWalkingViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (description.isNullOrEmpty()) {
            petWalkingViewModel.getPetWalking(context)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                PetWalkingHeader(
                    navController = navController,
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (!petWalkingUiState.errorMessage.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(R.drawable.dog_sad),
                            contentDescription = "dog",
                            modifier = Modifier.size(60.dp)
                        )
                        Text(
                            petWalkingUiState.errorMessage,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
                        )

                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(), color = Color.Transparent
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimens.small1),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (totalReviews > 0) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color(0xFFFFC700),
                                                modifier = Modifier.size(
                                                    24.dp
                                                )
                                            )
                                            Text(
                                                text = petWalkingUiState.averageReviews.toString(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.W500
                                            )
                                            Text(
                                                "~",
                                                style = MaterialTheme.typography.displaySmall,
                                                fontWeight = FontWeight.W400,
                                                modifier = Modifier.padding(horizontal = 3.dp)
                                            )
                                            Text(
                                                text = "($totalReviews ratings)",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.Gray
                                            )
                                        }

                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        discount?.let {
                                            Box(
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.discount_badge),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.tertiary,
                                                    modifier = Modifier
                                                        .size(55.dp)
                                                )
                                                Column(
                                                    modifier = Modifier
                                                        .padding(
                                                            9.dp
                                                        ),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = "$discount%",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                    Text(
                                                        text = "OFF",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                }
                                            }
                                        }

                                    }

                                }
                            }
                            item {
                                Text(
                                    "Book Dog Walking Near You",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(MaterialTheme.dimens.small1),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "We provide Background-Checked dog walker and real time GPS track of their walk",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.W500,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = MaterialTheme.dimens.small1),
                                    textAlign = TextAlign.Center
                                )
                            }



                            item {
                                FrequencySection(
                                    frequencies = frequencies,
                                    selectedFrequency = selectedFrequency ?: frequencies.first(),
                                    context = context,
                                    onFrequencySelected = { frequency ->
                                        petWalkingViewModel.onFrequencySelect(frequency)
                                    }
                                )
                            }

                            item {
                                if (selectedFrequency == Frequency.REPEAT_WEEKLY) {
                                    DateRangePickerSection(
                                        startDate = petWalkingUiState.dateRange?.startDate,
                                        endDate = petWalkingUiState.dateRange?.endDate,
                                        onStartDateSelect = { date ->
                                            petWalkingViewModel.onStartDateSelect(date)
                                        },
                                        onEndDateSelect = { date ->
                                            petWalkingViewModel.onEndDateSelect(date)
                                        }
                                    )
                                } else {
                                    SingleDatePickerSection(
                                        singleDate = petWalkingUiState.singleDate,
                                        onSingleDateSelect = { date ->
                                            petWalkingViewModel.onSingleDateSelect(date)
                                        }
                                    )
                                }
                            }

                            item {
                                if (selectedFrequency == Frequency.REPEAT_WEEKLY) {
                                    DaysSelectionSection(
                                        context = context,
                                        days = days,
                                        selectedDays = selectedDays,
                                        onDaysSelected = { day ->
                                            petWalkingViewModel.onDaySelect(day)
                                        },
                                        onEverythingSelect = { petWalkingViewModel.onEverythingSelected() }
                                    )
                                }
                            }

                            if (reviews.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "What Pet Parents Are Saying",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier
                                            .padding(horizontal = MaterialTheme.dimens.small1)
                                            .padding(top = 20.dp, bottom = 8.dp)
                                    )
                                }
                                item {
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        items(reviews) { review ->
                                            ReviewCard(
                                                modifier = Modifier.padding(
                                                    start = if (review == reviews.first()) MaterialTheme.dimens.small1
                                                    else 0.dp,
                                                    end = if (review == reviews.last()) MaterialTheme.dimens.small1
                                                    else 0.dp
                                                ),
                                                maxStars = 5,
                                                stars = review.stars.toDouble(),
                                                name = review.userName,
                                                review = review.description,
                                                date = review.createdAt,
                                                color = Color.White.copy(0.5f)
                                            )
                                        }
                                    }
                                }

                            }

                        }
                    }
                    PetWalkBottomSection(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onProceedClick = {
                            petWalkingViewModel.onProceedClick(navController)
                        },
                        changePetSelectionView = { changePetSelectionView(true) }
                    )
                }
            }
        }
        if (petWalkingUiState.isLoading) {
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
fun DaysSelectionSection(
    modifier: Modifier = Modifier,
    context: Context,
    selectedDays: List<Days>,
    onDaysSelected: (Days) -> Unit = {},
    onEverythingSelect: () -> Unit,
    days: List<Days>
) {

    val isEverythingSelected = selectedDays.size == days.size

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "For which days?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.small1)
            )
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.padding(end = MaterialTheme.dimens.small1)
            ) {
                Icon(
                    if (isEverythingSelected) Icons.Default.CheckCircle
                    else Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(21.dp)
                        .bounceClick {
                            onEverythingSelect()
                        },
                    tint = MaterialTheme.colorScheme.tertiaryContainer
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.dimens.small1,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                val isSelected = selectedDays.contains(day)
                Surface(
                    modifier = Modifier
                        .size(40.dp),
                    shape = CircleShape,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else Color.Transparent,
                    border = if (isSelected) null else BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onDaysSelected(day)
                            },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            day.toString(context),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.W500,
                            fontSize = 12.sp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.onTertiaryContainer
                            else Color.Black
                        )
                    }
                }

            }
        }

    }

}

@Composable
fun SingleDatePickerSection(
    modifier: Modifier = Modifier,
    singleDate: LocalDateTime? = null,
    onSingleDateSelect: (LocalDateTime) -> Unit = {}
) {
    Column(modifier = modifier.padding(vertical = MaterialTheme.dimens.small1)) {
        Text(
            "Dates",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateField(
                value = formatDateTime(
                    singleDate ?: LocalDateTime.now(),
                    "dd/MM/yyyy"
                ),
                onValueChange = {},
                placeHolder = "Date",
                endPadding = MaterialTheme.dimens.small1,
                startPadding = MaterialTheme.dimens.small1,
                onDateSelect = onSingleDateSelect,
                selectedDate = singleDate ?: LocalDateTime.now()
            )
        }

    }

}

@Composable
fun DateRangePickerSection(
    modifier: Modifier = Modifier,
    startDate: LocalDateTime? = null,
    onStartDateSelect: (LocalDateTime) -> Unit = {},
    endDate: LocalDateTime? = null,
    onEndDateSelect: (LocalDateTime) -> Unit = {}
) {
    Column(modifier = modifier.padding(vertical = MaterialTheme.dimens.small1)) {
        Text(
            "Dates",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.Center
            ) {
                DateField(
                    value = if (startDate != null) formatDateTime(
                        startDate,
                        "dd/MM/yyyy"
                    ) else "",
                    onValueChange = {},
                    placeHolder = "Start",
                    endPadding = 0.dp,
                    startPadding = MaterialTheme.dimens.small1,
                    onDateSelect = onStartDateSelect,
                    selectedDate = startDate ?: LocalDateTime.now()
                )
            }
            DateField(
                value = if (endDate != null) formatDateTime(endDate, "dd/MM/yyyy") else "",
                onValueChange = {},
                placeHolder = "End",
                endPadding = MaterialTheme.dimens.small1,
                startPadding = 6.dp,
                onDateSelect = onEndDateSelect,
                selectedDate = endDate ?: LocalDateTime.now()
            )
        }

    }

}

@Composable
fun DateField(
    modifier: Modifier = Modifier,
    placeHolder: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    onDateSelect: (LocalDateTime) -> Unit = {},
    selectedDate: LocalDateTime,
    endPadding: Dp,
    startPadding: Dp
) {

    var showDateSelector by remember { mutableStateOf(false) }

    if (showDateSelector) {
        DatePickerModal(
            onDateSelected = { date ->
                date?.let {
                    onDateSelect(it)
                }
                showDateSelector = false
            },
            onDismiss = {
                showDateSelector = false
            },
            selectedDate = selectedDate
        )

    }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(placeHolder, color = Color.Gray) },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(
                end = endPadding,
                start = startPadding
            ),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.LightGray.copy(0.4f),
            unfocusedContainerColor = Color.LightGray.copy(0.4f)
        ),
        trailingIcon = {
            Icon(
                painterResource(R.drawable.calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(21.dp)
                    .bounceClick {
                        showDateSelector = true
                    }
            )
        }
    )

}

@Composable
fun FrequencySection(
    modifier: Modifier = Modifier,
    frequencies: List<Frequency>,
    selectedFrequency: Frequency,
    context: Context,
    onFrequencySelected: (Frequency) -> Unit
) {
    Column(modifier) {
        Text(
            "How often do you need Dog Walking?",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small1)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            frequencies.forEach { frequency ->
                val isSelected = frequency == selectedFrequency
                Surface(
                    modifier = Modifier
                        .width(
                            width = 180.dp
                        )
                        .height(90.dp)
                        .clickable {
                            onFrequencySelected(frequency)
                        },
                    border = if (isSelected)
                        BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary)
                    else BorderStroke(0.dp, Color.Transparent),
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = if (isSelected) 8.dp else 0.dp
                ) {
                    Box(modifier = Modifier.background(Color.LightGray.copy(0.5f))) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(MaterialTheme.dimens.small1),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                painterResource(frequency.getIconRes()),
                                contentDescription = null,
                                modifier = Modifier.size(26.dp)
                            )
                            Text(
                                frequency.toString(context),
                                style = MaterialTheme.typography.titleSmall,
                                overflow = TextOverflow.Ellipsis
                            )
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
fun PetWalkingHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "We Walk Your Dog"
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
fun PetWalkBottomSection(
    modifier: Modifier = Modifier,
    onProceedClick: () -> Unit,
    changePetSelectionView: (Boolean) -> Unit
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
            Button(
                onClick = {
                    onProceedClick()
                    changePetSelectionView(true)
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
                    text = "Proceed",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }
}