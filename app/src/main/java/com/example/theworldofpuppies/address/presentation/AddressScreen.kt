package com.example.theworldofpuppies.address.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressType
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.core.presentation.util.formatPhoneNumber
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addressViewModel: AddressViewModel,
    addressUiState: AddressUiState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val addresses = rememberSaveable(addressUiState.addresses) { addressUiState.addresses }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            AddressHeader(scrollBehavior = scrollBehavior, navController = navController)
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = Color.Transparent
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
            ) {
                item {
                    Text(
                        "Select Delivery Address",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
                    )
                }
                item {
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
                        if (addressUiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        } else {
                            Text(
                                text = "Add New Address",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                    }

                }
                items(addresses, key = { it.id }) { address ->
                    AddressCard(address = address, addressViewModel = addressViewModel)
                }
            }
        }
    }
}

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,
    address: Address,
    addressViewModel: AddressViewModel
) {

    val image = when (address.addressType) {
        AddressType.HOME -> R.drawable.home
        AddressType.OFFICE -> R.drawable.office_bag
        AddressType.OTHER -> R.drawable.other
    }

    val isSelected = address.isSelected

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1)
            .clickable { addressViewModel.updateAddressSelection(address) },
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isSelected) 8.dp else 0.dp
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
                    Icon(
                        painterResource(image),
                        contentDescription = "Home",
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = address.addressType.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(5.dp)
                    )
                    if (isSelected) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "checked"
                            )
                        }
                    }
                }
                Text(
                    text = address.contactName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = formatPhoneNumber(address.contactNumber),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = "${address.houseNumber}, ${address.landmark}, ${address.city}, ${address.state}, ${address.pinCode}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.W500,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 10.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {

    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth(),
        title = {},
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.dimens.small1)
                        .size(
                            MaterialTheme.dimens.small1 + MaterialTheme.dimens.extraSmall.times(
                                3
                            )
                        )
                ) {
                    Icon(
                        painterResource(R.drawable.arrow_left_filled),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(21.dp)
                    )
                }
                Text(
                    text = "Address",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
                )
                IconButton(
                    onClick = { navController.navigate(Screen.CartScreen.route) },
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.extraSmall)
                ) {
                    Icon(
                        painterResource(R.drawable.bag_outline),
                        contentDescription = "Bag",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

        }
    )

}
