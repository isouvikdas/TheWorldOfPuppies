package com.example.theworldofpuppies.address.presentation

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.address.domain.AddressUiState
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.address.presentation.util.getAddressDescription
import com.example.theworldofpuppies.address.presentation.util.getIconRes
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatPhoneNumber
import com.example.theworldofpuppies.core.presentation.util.isSmallScreenHeight
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.presentation.DeleteDialog
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

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

    val addresses = addressUiState.addresses

    val context = LocalContext.current

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by addressViewModel.isRefreshing.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        addressViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                AddressHeader(scrollBehavior = scrollBehavior, navController = navController)
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = Color.Transparent
            ) {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        addressViewModel.forceLoad()
                    },
                    state = pullToRefreshState
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
                    ) {
                        item {
                            Text(
                                "Select Delivery Address",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
                            )
                        }

                        item {
                            Button(
                                onClick = {
                                    addressViewModel.onAddAddressClick(navController = navController)
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
                                    text = "Add New Address",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        if (addresses.isNotEmpty()) {
                            items(addresses, key = { it.id }) { address ->
                                AddressCard(
                                    address = address,
                                    addressViewModel = addressViewModel,
                                    isCheckoutScreen = false,
                                    navController = navController
                                )
                            }
                        } else if (addressUiState.error != null) {
                            item {
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
                                        addressUiState.error.toString(context),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W500),
                                    )
                                }
                            }
                        } else if (addressUiState.isLoading) {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }

                            }
                        }


                        item {
                            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium2))
                        }
                    }

                }
            }
        }

        // ✅ Show loading indicator when isLoading is true
        if (addressUiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent.copy(alpha = 0.2f))
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
fun AddressCard(
    modifier: Modifier = Modifier,
    address: Address,
    addressViewModel: AddressViewModel,
    isCheckoutScreen: Boolean,
    navController: NavController
) {

    val isSelected = address.isSelected

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteDialog(
            onDismiss = { showDeleteDialog = false },
            onDeleteConfirm = {
                    addressViewModel.deleteAddress(address.id)

            }
        )
    }

    Surface(
        modifier =
            if (isCheckoutScreen) modifier
                .fillMaxWidth()
                .wrapContentHeight()
            else modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = MaterialTheme.dimens.small1)
                .clickable { addressViewModel.updateAddressSelection(address) },
        border = if (isSelected)
            BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary)
        else BorderStroke(0.dp, Color.Transparent),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isCheckoutScreen) 0.dp else if (isSelected) 8.dp else 0.dp
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
                        painterResource(address.addressType.getIconRes()),
                        contentDescription = "address",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = address.addressType.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(5.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "checked",
                                tint = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "edit",
                            modifier = Modifier
                                .bounceClick {
                                    addressViewModel.onEditAddressClick(
                                        navController = navController,
                                        address = address
                                    )
                                }
                                .padding(start = 10.dp)
                                .size(22.dp)
                        )
                        if (!isCheckoutScreen) {
                            Icon(
                                Icons.Default.DeleteOutline,
                                contentDescription = "delete",
                                modifier = Modifier
                                    .bounceClick {
                                        showDeleteDialog = true
                                    }
                                    .padding(start = 10.dp)
                                    .size(22.dp)
                            )

                        }

                    }
                }
                Text(
                    text = address.contactName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = formatPhoneNumber(address.contactNumber),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.W500
                )

                val addressDescription = getAddressDescription(address)

                Text(
                    text = addressDescription,
                    style = MaterialTheme.typography.bodySmall,
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
    navController: NavController,
) {
    //used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Address"
    ) {
    }
}
