package com.example.theworldofpuppies.address.presentation

import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressDetailUiState
import com.example.theworldofpuppies.address.domain.AddressType
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.address.presentation.util.getIconRes
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addressViewModel: AddressViewModel,
    addressDetailUiState: AddressDetailUiState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val addressTypes = addressDetailUiState.addressTypeList
    val selectedAddressType = addressDetailUiState.selectedAddressType

    val id = addressDetailUiState.id
    val contactName = addressDetailUiState.contactName
    val contactNumber = addressDetailUiState.contactNumber
    val houseNumber = addressDetailUiState.houseNumber
    val street = addressDetailUiState.street
    val landmark = addressDetailUiState.landmark
    val city = addressDetailUiState.city
    val state = addressDetailUiState.state
    val country = addressDetailUiState.country
    val pinCode = addressDetailUiState.pinCode

    val contactNameError = addressDetailUiState.contactNameError
    val contactNumberError = addressDetailUiState.contactNumberError
    val cityError = addressDetailUiState.cityError
    val pinCodeError = addressDetailUiState.pinCodeError
    val streetError = addressDetailUiState.streetError
    val landmarkError = addressDetailUiState.landmarkError


    val context = LocalContext.current

    LaunchedEffect(Unit) {
        addressViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            AddressDetailHeader(scrollBehavior = scrollBehavior, navController = navController)
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {

                AddressForm(
                    addressTypes = addressTypes,
                    addressViewModel = addressViewModel,
                    selectedAddressType = selectedAddressType,
                    contactName = contactName,
                    onContactNameChange = { addressViewModel.updateContactName(it) },
                    contactNumber = contactNumber,
                    onContactNumberChange = { addressViewModel.updateContactNumber(it) },
                    houseNumber = houseNumber,
                    onHouseNumberChange = { addressViewModel.updateHouseNumber(it) },
                    landmark = landmark,
                    onLandmarkChange = { addressViewModel.updateLandmark(it) },
                    street = street,
                    onStreetChange = { addressViewModel.updateStreet(it) },
                    city = city,
                    onCityChange = { addressViewModel.updateCity(it) },
                    pinCode = pinCode,
                    onPinCodeChange = { addressViewModel.updatePinCode(it) },
                    state = state,
                    onStateChange = { },
                    country = country,
                    onCountryChange = { },
                    contactNameError = contactNameError,
                    contactNumberError = contactNumberError,
                    pinCodeError = pinCodeError,
                    streetError = streetError,
                    landmarkError = landmarkError,
                    cityError = cityError
                )

                AddressDetailBottomSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    addressViewModel = addressViewModel,
                    navController = navController,
                    addressDetailUiState = addressDetailUiState,
                    updatedAddress = Address(
                        id = id,
                        contactNumber = contactNumber,
                        contactName = contactName,
                        houseNumber = houseNumber,
                        street = street,
                        landmark = landmark,
                        city = city,
                        state = state,
                        pinCode = pinCode,
                        country = country,
                        addressType = selectedAddressType
                    )

                )
            }
        }
    }
}


@Composable
fun AddressForm(
    modifier: Modifier = Modifier,
    selectedAddressType: AddressType,
    addressTypes: List<AddressType>,
    addressViewModel: AddressViewModel,
    contactName: String,
    onContactNameChange: (String) -> Unit,
    contactNumber: String,
    onContactNumberChange: (String) -> Unit,
    houseNumber: String,
    onHouseNumberChange: (String) -> Unit,
    landmark: String,
    onLandmarkChange: (String) -> Unit,
    street: String,
    onStreetChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    pinCode: String,
    onPinCodeChange: (String) -> Unit,
    state: String,
    onStateChange: (String) -> Unit,
    country: String,
    onCountryChange: (String) -> Unit,
    contactNameError: String?,
    contactNumberError: String?,
    pinCodeError: String?,
    streetError: String?,
    landmarkError: String?,
    cityError: String?
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.dimens.small1),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            AddressField(
                heading = "Contact Name",
                value = contactName,
                onValueChange = onContactNameChange,
                errorMessage = contactNameError
            )
        }
        item {
            AddressField(
                heading = "Contact Number",
                value = contactNumber,
                onValueChange = onContactNumberChange,
                errorMessage = contactNumberError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        item {
            AddressField(
                heading = "House Number",
                value = houseNumber,
                onValueChange = onHouseNumberChange,
                isOptional = true
            )
        }
        item {
            AddressField(
                heading = "Landmark",
                value = landmark,
                onValueChange = onLandmarkChange,
                errorMessage = landmarkError
            )
        }
        item {
            AddressField(
                heading = "Street",
                value = street,
                onValueChange = onStreetChange,
                errorMessage = streetError
            )
        }
        item {
            AddressField(
                heading = "City",
                value = city,
                onValueChange = onCityChange,
                errorMessage = cityError
            )
        }
        item {
            AddressField(
                heading = "Pin Code",
                value = pinCode,
                onValueChange = onPinCodeChange,
                errorMessage = pinCodeError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        item {
            AddressField(
                heading = "State",
                value = state,
                onValueChange = onStateChange,
                readOnly = true
            )
        }
        item {
            AddressField(
                heading = "Country",
                value = country,
                onValueChange = onCountryChange,
                readOnly = true
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                addressTypes.forEach { addressType ->
                    val isSelected = selectedAddressType == addressType
                    FilterChip(
                        onClick = {
                            addressViewModel.selectAddressType(addressType)
                        },
                        label = {
                            Text(
                                addressType.name,
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
                        border = BorderStroke(0.5.dp, color = Color.Gray),
                        leadingIcon = {
                            Icon(
                                painterResource(addressType.getIconRes()),
                                contentDescription = "address type",
                                modifier = Modifier.size(22.dp),
                                tint = if (isSelected) Color.White else Color.Black
                            )
                        }
                    )

                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
        }
    }
}

@Composable
fun AddressDetailBottomSection(
    modifier: Modifier = Modifier,
    addressViewModel: AddressViewModel,
    navController: NavController,
    addressDetailUiState: AddressDetailUiState,
    updatedAddress: Address,
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = Color.White,
        shadowElevation = 5.dp,
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
                    addressViewModel.addOrUpdateAddress(
                        navController = navController,
                        address = updatedAddress
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
                ),
            ) {
                if (addressDetailUiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                } else {
                    Text(
                        text = "Save Address",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}

@Composable
fun AddressField(
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
fun AddressDetailHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController,
) {
    //used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Address Details"
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

fun validateFields(
    contactName: String,
    contactNumber: String,
    city: String,
    pinCode: String
): Boolean {
    var isValid = true

    if (contactName.isBlank()) {
        isValid = false
        "Name cannot be empty"
    } else null

    if (contactNumber.isBlank()) {
        isValid = false
        "Contact number is required"
    } else null

    if (city.isBlank()) {
        isValid = false
        "City is required"
    } else null

    if (pinCode.isBlank()) {
        isValid = false
        "Pin code is required"
    } else null

    return isValid
}

