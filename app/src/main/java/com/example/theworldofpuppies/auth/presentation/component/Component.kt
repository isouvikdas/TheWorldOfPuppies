package com.example.theworldofpuppies.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.rejowan.ccpc.CCPUtils
import com.rejowan.ccpc.Country
import com.rejowan.ccpc.CountryCodePickerTextField

@Composable
fun TextTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textColor: Color,
    placeHolder: String,
    hint: String,
    fontSize: TextUnit,
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
                fontSize = fontSize,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            if (isNeeded) {
                Text(
                    text = "*",
                    fontSize = fontSize,
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
            textStyle = LocalTextStyle.current.copy(
                color = textColor,
                fontSize = fontSize
            ),
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
    fontSize: TextUnit,
    isNeeded: Boolean,
    keyBoardType: KeyboardType = KeyboardType.Number,
    textColor: Color,
    country: Country,
    number: String,
    onNumberChange: (String) -> Unit,
    onCountryChange: (Country) -> Unit,
    isVisible: Boolean

) {

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
                fontSize = fontSize,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            if (isNeeded) {
                Text(
                    text = "*",
                    fontSize = fontSize,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 1.dp, start = 2.dp)
                )
            }
        }
        CountryCodePickerTextField(
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            textStyle = LocalTextStyle.current.copy(
                color = textColor,
                fontSize = fontSize
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
            trailingIcon = {
                if (isVisible)
                    IconButton(onClick = { onNumberChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear, contentDescription = "Clear"
                        )
                    }
            },
            placeholder = {
                Text(
                    text = "Phone Number", color = Color.Gray
                )
            },
            showError = true,
            shape = RoundedCornerShape(12.dp),
            onValueChange = { _, value, _ ->
                onNumberChange(value)
            },
            number = number,
            showSheet = true,
            selectedCountry = country
        )
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