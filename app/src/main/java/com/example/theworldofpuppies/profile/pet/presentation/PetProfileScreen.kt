package com.example.theworldofpuppies.profile.pet.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetProfileScreen(modifier: Modifier = Modifier, navController: NavController) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val genders = listOf("Female", "Male")
    var selectedGender by rememberSaveable { mutableStateOf("") }

    val aggressions = listOf("Low", "Medium", "High")
    var selectedAggression by rememberSaveable { mutableStateOf("") }

    var checked by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            PetProfileHeader(
                scrollBehavior = scrollBehavior,
                navController = navController
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = Color.Transparent
            ) {
                Box(modifier = Modifier.fillMaxSize()) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = MaterialTheme.dimens.small1),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        item {
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(
                                    "Gender",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    genders.forEach { gender ->
                                        val isSelected = gender == selectedGender
                                        FilterChip(
                                            onClick = {
                                                selectedGender = gender
                                            },
                                            label = {
                                                Text(
                                                    gender,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            },
                                            selected = isSelected,
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.dimens.small1.div(2))
                                                .animateItem(),
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                                    0.8f
                                                ),
                                                selectedLabelColor = Color.White
                                            ),
                                            border = BorderStroke(0.5.dp, color = Color.Gray)
                                        )

                                    }

                                }

                            }
                        }

                        item {
                            ProfileScreenField(
                                heading = "Name",
                                value = "",
                            )
                        }

                        item {
                            ProfileScreenField(
                                heading = "Breed",
                                value = "",
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.5f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ProfileScreenField(
                                        modifier = Modifier.padding(end = 2.dp),
                                        heading = "Age",
                                        value = "",
                                        placeHolder = "Age in months",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                                ProfileScreenField(
                                    modifier = Modifier.padding(start = 2.dp),
                                    heading = "Weight",
                                    value = "",
                                    placeHolder = "Weight in kg",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        }

                        item {
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(
                                    "Aggression",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    aggressions.forEach { aggression ->
                                        val isSelected = aggression == selectedAggression
                                        FilterChip(
                                            onClick = {
                                                selectedAggression = aggression
                                            },
                                            label = {
                                                Text(
                                                    aggression,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            },
                                            selected = isSelected,
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.dimens.small1.div(2))
                                                .animateItem(),
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                                                    0.8f
                                                ),
                                                selectedLabelColor = Color.White
                                            ),
                                            border = BorderStroke(0.5.dp, color = Color.Gray)
                                        )

                                    }

                                }

                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Is Vaccinated",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Switch(
                                    checked = checked,
                                    onCheckedChange = { it ->
                                        checked = it
                                    },
                                    thumbContent = if (checked) {
                                        {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(SwitchDefaults.IconSize),
                                            )
                                        }
                                    } else {
                                        null
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                                        checkedTrackColor = MaterialTheme.colorScheme.tertiary.copy(
                                            0.3f
                                        ),
                                        uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                                        uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary,
                                    )
                                )
                            }
                        }


                    }

                    PetProfileBottomSection(modifier = Modifier.align(Alignment.BottomCenter))
                }
            }

        }
    }
}

@Composable
fun <T> ProfileFilterItem(modifier: Modifier = Modifier, heading: String, types: List<T>, selectedType: MutableState<T>) {
    Column(verticalArrangement = Arrangement.Center) {
        Text(
            heading,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            types.forEach { type ->
                val isSelected = type == selectedType.value
                FilterChip(
                    onClick = {
                        selectedType.value = type
                    },
                    label = {
                        Text(
                            type.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    selected = isSelected,
                    shape = RoundedCornerShape(16.dp),
                    modifier = modifier
                        .padding(end = MaterialTheme.dimens.small1.div(2)),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                            0.8f
                        ),
                        selectedLabelColor = Color.White
                    ),
                    border = BorderStroke(0.5.dp, color = Color.Gray)
                )

            }

        }

    }

}

@Composable
fun ProfileScreenField(
    modifier: Modifier = Modifier,
    heading: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    defaultValue: String = "",
    isOptional: Boolean = false,
    value: String = "",
    onValueChange: (String) -> Unit = { "" },
    errorMessage: String? = null,
    placeHolder: String = ""
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = heading,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (isOptional) {
                Text(
                    text = "(Optional)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            isError = errorMessage != null,
            supportingText = {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.W500
                    )
                }
            },
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.LightGray.copy(0.4f),
                unfocusedContainerColor = Color.LightGray.copy(0.4f),
                errorContainerColor = Color.LightGray.copy(0.4f)
            ),
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.titleMedium,
            placeholder = {
                if (readOnly) Text(
                    text = defaultValue,
                    style = MaterialTheme.typography.titleMedium
                )
                else if (placeHolder.isNotEmpty())
                    Text(
                        text = placeHolder,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                else Text(
                    text = "Enter $heading",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetProfileHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Pet Profile"
    ) {

        Icon(
            painterResource(R.drawable.bag_outline),
            contentDescription = "Profile",
            modifier = Modifier
                .size(21.dp)
                .bounceClick {
                    navController.navigate(Screen.CartScreen.route)
                }
        )
    }
}

@Composable
fun PetProfileBottomSection(modifier: Modifier = Modifier) {
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
                    text = "Save",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}
