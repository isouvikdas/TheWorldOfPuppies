package com.example.theworldofpuppies.profile.presentation

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.util.formatPhoneNumber
import com.example.theworldofpuppies.profile.pet.domain.PetListUiState
import com.example.theworldofpuppies.profile.pet.presentation.PetProfileViewModel
import com.example.theworldofpuppies.profile.user.domain.UpdateUserUiState
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileViewModel: ProfileViewModel,
    petProfileViewModel: PetProfileViewModel,
    updateUserUiState: UpdateUserUiState,
    petListUiState: PetListUiState
) {

    val pet = if (petListUiState.pets.isNotEmpty()) {
        petListUiState.pets.first()
    } else {
        null
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            item {
                ProfileSection(
                    onUserProfileClick = {
                        profileViewModel.onUserProfileClick(navController)
                    },
                    userImageUri = updateUserUiState.imageUri,
                    username = updateUserUiState.username,
                    userDescription = formatPhoneNumber(updateUserUiState.phoneNumber),
                    onPetProfileClick = {
                        petProfileViewModel.resetPetUiState()
                        petProfileViewModel.changePetSelectionView(false)
                        profileViewModel.onPetProfileClick(navController)

                    },
                    petImageUri = pet?.downloadUrl?.toUri(),
                    petName = pet?.name,
                    petDescription = pet?.breed?.breedName
                )
            }
            item {
                AccountAndMembershipSection(
                    onMembershipClick = {
                        profileViewModel.onMembershipClick(navController)
                    },
                    onAddressClick = { profileViewModel.onAddressClick(navController) }
                )
            }
            item {
                CommunicationAndEngagementSection(
                    onReferClick = {
                        profileViewModel.onReferClick(navController)
                    },
                    onMessageClick = { }
                )
            }
            item {
                OrderAndRewardSection(
                    onOrderClick = { profileViewModel.onOrderClick(navController = navController) },
                    onCouponClick = {},
                    onBookingClick = { profileViewModel.onBookingClick(navController) }
                )
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = MaterialTheme.dimens.small2))
            }

            item {
                SignOutSection()
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = MaterialTheme.dimens.large3))
            }
        }
    }
}


@Composable
fun SignOutSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .width(MaterialTheme.dimens.large3.times(2))
                .height(45.dp)
                .padding(horizontal = MaterialTheme.dimens.small1),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.errorContainer,
                containerColor = Color.Transparent
            ),
            border = BorderStroke(
                width = 1.3.dp,
                color = MaterialTheme.colorScheme.errorContainer
            )

        ) {
            Text(
                "Sign Out",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

    }
}

@Composable
fun ProfileSection(
    onUserProfileClick: () -> Unit,
    onPetProfileClick: () -> Unit,
    userImageUri: Uri? = null,
    username: String? = null,
    userDescription: String? = null,
    petImageUri: Uri? = null,
    petName: String? = null,
    petDescription: String? = null
) {
    Text(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.small1)
            .padding(top = MaterialTheme.dimens.small1, bottom = 6.dp),
        text = "Profiles",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.LightGray.copy(0.4f),
        shape = RoundedCornerShape(MaterialTheme.dimens.small1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            ProfileItem(
                image = userImageUri ?: Uri.EMPTY,
                title = username ?: "",
                description = userDescription ?: "Personal Info",
                onClick = {
                    onUserProfileClick()
                },
                errorImage = R.drawable.profile
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            ProfileItem(
                image = petImageUri ?: Uri.EMPTY,
                title = petName ?: "Pet",
                description = petDescription ?: "Personal Info",
                onClick = {
                    onPetProfileClick()
                },
                errorImage = R.drawable.pet_profile
            )
        }
    }
}

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    image: Uri?,
    title: String,
    description: String,
    onClick: () -> Unit,
    errorImage: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(40.dp),
            shape = CircleShape,
            color = Color.LightGray.copy(0.5f)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                model = image,
                contentDescription = "Pet Profile Pic",
                contentScale = ContentScale.Crop,
                error = painterResource(errorImage)
            )
        }
        Column(modifier = Modifier.padding(start = MaterialTheme.dimens.small1)) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                description,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier
                        .size(11.dp)
                )
            }
        }
    }
}

@Composable
fun AccountAndMembershipSection(
    onMembershipClick: () -> Unit,
    onAddressClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.small1)
            .padding(top = MaterialTheme.dimens.small1, bottom = 6.dp),
        text = "Account & Membership",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.LightGray.copy(0.4f),
        shape = RoundedCornerShape(MaterialTheme.dimens.small1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            AccountAndMembershipItem(
                image = R.drawable.membership,
                title = "Membership",
                onClick = {
                    onMembershipClick()
                }
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            AccountAndMembershipItem(
                image = R.drawable.address,
                title = "Address",
                onClick = {
                    onAddressClick()
                }
            )
        }
    }
}

@Composable
fun AccountAndMembershipItem(
    modifier: Modifier = Modifier,
    image: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(27.dp),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = MaterialTheme.dimens.small1)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier
                        .size(11.dp)
                )
            }
        }
    }
}

@Composable
fun CommunicationAndEngagementSection(
    onMessageClick: () -> Unit,
    onReferClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.small1)
            .padding(top = MaterialTheme.dimens.small1, bottom = 6.dp),
        text = "Communication & Engagement",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.LightGray.copy(0.4f),
        shape = RoundedCornerShape(MaterialTheme.dimens.small1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            CommunicationAndMembershipItem(
                image = R.drawable.message,
                title = "Messages",
                onClick = {
                    onMessageClick()
                },
                isDisabled = true
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            CommunicationAndMembershipItem(
                image = R.drawable.refer,
                title = "Refer & Earn",
                onClick = {
                    onReferClick()
                }
            )
        }
    }
}

@Composable
fun CommunicationAndMembershipItem(
    modifier: Modifier = Modifier,
    image: Int,
    title: String,
    onClick: () -> Unit,
    isDisabled: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(27.dp),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
            color = if (isDisabled) Color.Gray else Color.Black
        )
        if (isDisabled) {
            Text(
                "COMING SOON",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
                color = Color.Gray,
                fontStyle = FontStyle.Italic
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = !isDisabled) { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier.size(11.dp),
                    tint = if (isDisabled) Color.Gray else Color.Black
                )
            }
        }
    }
}

@Composable
fun OrderAndRewardSection(
    onOrderClick: () -> Unit,
    onBookingClick: () -> Unit,
    onCouponClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.small1)
            .padding(top = MaterialTheme.dimens.small1, bottom = 6.dp),
        text = "Orders & Rewards",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.LightGray.copy(0.4f),
        shape = RoundedCornerShape(MaterialTheme.dimens.small1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            OrderAndRewardsItem(
                image = R.drawable.booking,
                title = "Bookings",
                onClick = {
                    onBookingClick()
                }
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            OrderAndRewardsItem(
                image = R.drawable.order,
                title = "Orders",
                onClick = {
                    onOrderClick()
                }
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            OrderAndRewardsItem(
                image = R.drawable.reward,
                title = "Coupons",
                onClick = {
                    onCouponClick()
                }
            )
        }
    }
}

@Composable
fun OrderAndRewardsItem(
    modifier: Modifier = Modifier,
    image: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimens.small1, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(27.dp),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = MaterialTheme.dimens.small1)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier.size(11.dp)
                )
            }
        }
    }
}




