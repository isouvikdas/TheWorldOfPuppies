package com.example.theworldofpuppies.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.rejowan.ccpc.CCPUtils
import com.rejowan.ccpc.CCPUtils.Companion.getEmojiFlag
import com.rejowan.ccpc.CCPValidator
import com.rejowan.ccpc.Country
import com.rejowan.ccpc.PickerCustomization
import com.rejowan.ccpc.ViewCustomization
import kotlinx.coroutines.launch

@Composable
fun TextTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    hint: String,
    isNeeded: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    isVisible: Boolean
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = placeHolder,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            if (isNeeded) {
                Text(
                    text = "*",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 1.dp, start = 2.dp)
                )
            }
        }
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            placeholder = {
                Text(
                    text = hint,
                    color = Color.Gray
                )
            },
            textStyle = MaterialTheme.typography.titleMedium,

            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = {
                if (isVisible)
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
            }
        )
    }

}

@Composable
fun PhoneNumberField(
    modifier: Modifier = Modifier,
    placeHolder: String,
    isNeeded: Boolean,
    keyBoardType: KeyboardType = KeyboardType.Number,
    textColor: Color,
    country: Country,
    number: String,
    onValueChange: (countryCode: String, value: String, isValid: Boolean) -> Unit,
    onCountryChange: (Country) -> Unit,
    isVisible: Boolean,
    countryList: List<Country>,
    viewCustomization: ViewCustomization = ViewCustomization(),
    pickerCustomization: PickerCustomization = PickerCustomization(),
    backgroundColor: Color = Color.White,
    itemPadding: Int = 10,
) {

    val context = LocalContext.current

    val validatePhoneNumber = remember(context) {
        CCPValidator(context = context)
    }

    var isNumberValid: Boolean by rememberSaveable(country, number) {
        mutableStateOf(
            validatePhoneNumber(
                number = number,
                countryCode = country.countryCode
            ),
        )
    }

    if (!LocalInspectionMode.current) {
        CCPUtils.getCountryAutomatically(context = LocalContext.current).let {
            it?.let {
                onCountryChange(it)
            }
        }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = placeHolder,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            if (isNeeded) {
                Text(
                    text = "*",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 1.dp, start = 2.dp)
                )
            }
        }

        OutlinedTextField(
            value = number,
            onValueChange = { newValue ->
                isNumberValid = validatePhoneNumber(
                    number = newValue,
                    countryCode = country.countryCode
                )
                onValueChange(country.countryCode, newValue, isNumberValid)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            textStyle = MaterialTheme.typography.titleMedium,
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
            leadingIcon = {
                CountryCodePicker(
                    selectedCountry = country,
                    countryList = countryList,
                    onCountrySelected = { selectedCountry ->
                        onCountryChange(selectedCountry)
                        isNumberValid = validatePhoneNumber(
                            number = number,
                            countryCode = selectedCountry.countryCode
                        )
                        onValueChange(selectedCountry.countryCode, number, isNumberValid)
                    },
                    viewCustomization = viewCustomization,
                    pickerCustomization = pickerCustomization,
                    backgroundColor = backgroundColor,
                    textStyle = LocalTextStyle.current,
                    itemPadding = itemPadding
                )
            },
            trailingIcon = {
                if (isVisible)
                    IconButton(onClick = {
                        onValueChange(country.countryCode, "", false)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
            },
            placeholder = {
                Text(
                    text = "Phone Number",
                    color = Color.Gray
                )
            },
            shape = RoundedCornerShape(12.dp),
        )
    }
}

@Composable
fun CountryCodePicker(
    modifier: Modifier = Modifier,
    selectedCountry: Country = Country.Bangladesh,
    countryList: List<Country> = Country.getAllCountries(),
    onCountrySelected: (Country) -> Unit,
    viewCustomization: ViewCustomization = ViewCustomization(),
    pickerCustomization: PickerCustomization = PickerCustomization(),
    backgroundColor: Color = Color.White,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    itemPadding: Int = 10,
) {

    var country by remember { mutableStateOf(selectedCountry) }
    var isPickerOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.clickable {
            isPickerOpen = true
        },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CountryView(
            country = country,
            showFlag = viewCustomization.showFlag,
            showCountryIso = viewCustomization.showCountryIso,
            showCountryName = viewCustomization.showCountryName,
            showCountryCode = viewCustomization.showCountryCode,
            showArrow = viewCustomization.showArrow,
            textStyle = textStyle,
            itemPadding = itemPadding,
            clipToFull = viewCustomization.clipToFull
        )

        if (isPickerOpen) {
            CountryPickerBottomSheet(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                onDismissRequest = { isPickerOpen = false },
                onItemClicked = {
                    onCountrySelected(it)
                    country = it
                    isPickerOpen = false

                },
                textStyle = textStyle,
                listOfCountry = countryList,
                pickerCustomization = pickerCustomization,
                itemPadding = itemPadding,
                backgroundColor = backgroundColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onItemClicked: (item: Country) -> Unit,
    textStyle: TextStyle = TextStyle(),
    listOfCountry: List<Country>,
    pickerCustomization: PickerCustomization = PickerCustomization(),
    itemPadding: Int = 10,
    backgroundColor: Color = Color.White,
) {

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var value by remember { mutableStateOf("") }


    val filteredCountries by remember(context, value) {
        derivedStateOf {
            if (value.isEmpty()) {
                listOfCountry
            } else {
                Country.searchCountry(value, listOfCountry)
            }
        }

    }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        containerColor = backgroundColor
    ) {
        Surface(
            color = backgroundColor, modifier = modifier
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(itemPadding.dp))

                CountryHeaderSheet(title = pickerCustomization.headerTitle)

                Spacer(modifier = Modifier.height(itemPadding.dp))

                CountrySearch(
                    value = value,
                    onValueChange = { value = it },
                    textStyle = textStyle,
                    hint = pickerCustomization.searchHint,
                    showClearIcon = pickerCustomization.showSearchClearIcon,
                    requestFocus = false,
                    onFocusChanged = {
                        if (it.hasFocus) {
                            scope.launch {
                                sheetState.expand()
                            }
                        }

                    })

                Spacer(modifier = Modifier.height(itemPadding.dp))


                LazyColumn {
                    items(filteredCountries, key = { it.countryName }) { countryItem ->
                        HorizontalDivider(color = pickerCustomization.dividerColor)
                        CountryUI(
                            country = countryItem,
                            onCountryClicked = { onItemClicked(countryItem) },
                            countryTextStyle = textStyle,
                            itemPadding = itemPadding,
                            showCountryIso = pickerCustomization.showCountryIso,
                            showCountryCode = pickerCustomization.showCountryCode,
                            showCountryFlag = pickerCustomization.showFlag,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun CountrySearch(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    hint: String = "Search Country",
    showClearIcon: Boolean = true,
    requestFocus: Boolean = true,
    onFocusChanged: (FocusState) -> Unit = {}
) {

    val requester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        if (requestFocus) {
            requester.requestFocus()
        } else {
            requester.freeFocus()
        }
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(requester)
            .onFocusChanged { onFocusChanged(it) },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(text = hint)
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if (showClearIcon && value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Outlined.Clear, contentDescription = "Clear")
                }
            }
        }

    )


}


@Composable
internal fun CountryHeaderSheet(
    title: String = "Select Country"
) {

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {


        Text(
            text = title,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            textAlign = TextAlign.Center
        )


    }


}

@Composable
internal fun CountryUI(
    country: Country,
    onCountryClicked: () -> Unit,
    showCountryFlag: Boolean = true,
    showCountryIso: Boolean = false,
    showCountryCode: Boolean = true,
    countryTextStyle: TextStyle,
    itemPadding: Int = 10

) {

    Row(
        Modifier
            .clickable(onClick = { onCountryClicked() })
            .padding(itemPadding.dp, (itemPadding * 1.5).dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically

    ) {

        val countryString = if (showCountryFlag && showCountryIso) {
            (getEmojiFlag(country.countryIso)) + "  " + country.countryName + "  (" + country.countryIso + ")"
        } else if (showCountryFlag) {
            (getEmojiFlag(country.countryIso)) + "  " + country.countryName
        } else if (showCountryIso) {
            country.countryName + "  (" + country.countryIso + ")"
        } else {
            country.countryName
        }

        Text(
            text = countryString,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
            style = countryTextStyle,
            overflow = TextOverflow.Ellipsis
        )

        if (showCountryCode) {
            Text(
                text = country.countryCode, style = countryTextStyle
            )
        }

    }


}


@Composable
internal fun CountryView(
    country: Country,
    textStyle: TextStyle,
    showFlag: Boolean,
    showCountryIso: Boolean,
    showCountryName: Boolean,
    showCountryCode: Boolean,
    showArrow: Boolean,
    itemPadding: Int = 10,
    clipToFull: Boolean = false
) {

    Row(
        modifier = Modifier.padding(itemPadding.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        if (showFlag) {
            Text(
                text = getEmojiFlag(country.countryIso),
                modifier = Modifier.padding(start = 5.dp, end = 10.dp),
                style = textStyle
            )
        }

        if (showCountryName) {
            Text(
                text = country.countryName,
                modifier = Modifier.padding(end = 10.dp),
                style = textStyle
            )
        }

        if (showCountryIso) {
            Text(
                text = "(" + country.countryIso + ")",
                modifier = Modifier.padding(end = 20.dp),
                style = textStyle
            )
        }

        if (clipToFull) {
            Spacer(modifier = Modifier.weight(1f))
        }


        if (showCountryCode) {
            Text(
                text = country.countryCode,
                modifier = Modifier.padding(end = 5.dp),
                style = textStyle
            )
        }

        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
            )
        }


    }


}


@Preview
@Composable
private fun PhoneNumberFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            PhoneNumberField(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp),
//                placeHolder = "Phone Number",
//                fontSize = 18.sp,
//                isNeeded = true,
//                textColor = Color.Black
//            )
        }
    }
}