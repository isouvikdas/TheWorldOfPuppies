package com.example.theworldofpuppies.profile.user.presentation

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.formatPhoneNumber
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.presentation.PetProfileBottomSection
import com.example.theworldofpuppies.profile.pet.presentation.ProfileCircle
import com.example.theworldofpuppies.profile.pet.presentation.ProfileScreenField
import com.example.theworldofpuppies.profile.user.domain.UpdateUserUiState
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

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

    val isRefreshing = updateUserUiState.isRefreshing
    val pullToRefreshState = rememberPullToRefreshState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                updateUserViewModel.onPictureChange(it)
            }
        }
    )

    LaunchedEffect(Unit) {
        updateUserViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val imageUri = updateUserUiState.imageUri
    val username = updateUserUiState.username
    val email = updateUserUiState.email
    val phoneNumber = updateUserUiState.phoneNumber

    val usernameError = updateUserUiState.usernameError
    val emailError = updateUserUiState.emailError

    val isLoading = updateUserUiState.isLoading


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
                            onRefresh = {
                                updateUserViewModel.loadUserProfile(true)
                            },
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
                                        value = formatPhoneNumber(phoneNumber),
                                        onValueChange = {},
                                        errorMessage = "",
                                        readOnly = true
                                    )
                                }
                            }
                        }

                        UpdateUserBottomSection(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            updateUserViewModel = updateUserViewModel,
                            context = context,
                            navController = navController
                        )
                    }
                }
            }
        }

        if (isLoading) {
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

@Composable
fun UpdateUserBottomSection(
    modifier: Modifier = Modifier,
    updateUserViewModel: UpdateUserViewModel,
    context: Context,
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
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            Button(
                onClick = {
                    updateUserViewModel.saveProfile(context, navController)
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}

