package com.example.theworldofpuppies.profile.user.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.presentation.ProfileCircle
import com.example.theworldofpuppies.profile.pet.presentation.ProfileScreenField
import com.example.theworldofpuppies.profile.user.domain.UpdateUserUiState
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    updateUserViewModel: UpdateUserViewModel,
    updateUserUiState: UpdateUserUiState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val context = LocalContext.current

    val isRefreshing = updateUserUiState.isLoading
    val pullToRefreshState = rememberPullToRefreshState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                updateUserViewModel.onPictureChange(it)
            }
        }
    )

    val imageUri = updateUserUiState.imageUri
    val username = updateUserUiState.username
    val email = updateUserUiState.email
    val phoneNumber = updateUserUiState.phoneNumber

    val usernameError = updateUserUiState.usernameError
    val emailError = updateUserUiState.emailError

    val isValidEmail = remember(email) {
        val gmailPattern = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com\$")
        gmailPattern.matches(email)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                UpdateUserProfileHeader(
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
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { },
                            state = pullToRefreshState
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = MaterialTheme.dimens.small1),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    ProfileCircle(
                                        onClick = {
                                            imagePicker.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        selectedImageUri = imageUri,
                                        errorImage = R.drawable.profile,
                                        errorMessage = ""
                                    )
                                }

                                // Name
                                item {
                                    ProfileScreenField(
                                        heading = "Name",
                                        value = username,
                                        onValueChange = { name ->
                                            updateUserViewModel.onNameChange(
                                                name
                                            )
                                        },
                                        errorMessage = usernameError
                                    )
                                }

                                // email
                                item {
                                    ProfileScreenField(
                                        heading = "Email",
                                        value = email,
                                        onValueChange = { email ->
                                            updateUserViewModel.onEmailChange(
                                                email
                                            )
                                        },
                                        errorMessage = emailError
                                    )
                                }

                                // email
                                item {
                                    ProfileScreenField(
                                        heading = "Phone Number",
                                        value = phoneNumber,
                                        onValueChange = {},
                                        errorMessage = emailError,
                                        readOnly = true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateUserProfileHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Update Profile"
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
