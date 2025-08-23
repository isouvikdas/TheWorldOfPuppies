package com.example.theworldofpuppies.profile.presentation

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
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
                    onUserProfileClick = {},
                    onPetProfileClick = { profileViewModel.onPetProfileClick(navController) }
                )
            }
            item {
                AccountAndMembershipSection(
                    onMembershipClick = {},
                    onAddressClick = { profileViewModel.onAddressClick(navController) }
                )
            }
            item {
                CommunicationAndEngagementSection(
                    onReferClick = {},
                    onMessageClick = { profileViewModel.onMessageClick(navController) }
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
                Spacer(modifier = Modifier.padding(bottom = MaterialTheme.dimens.small3))
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
    OutlinedButton(
        onClick = { },
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.buttonHeight)
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

//@Composable
//fun SignOutSection(onClick: () -> Unit) {
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .padding(horizontal = MaterialTheme.dimens.small1)
//            .clickable { onClick() },
//        color = Color.LightGray.copy(0.4f),
//        shape = RoundedCornerShape(MaterialTheme.dimens.small1)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = MaterialTheme.dimens.small1.times(2))
//                .padding(vertical = MaterialTheme.dimens.small1),
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                painter = painterResource(R.drawable.logout_outline),
//                contentDescription = "logout",
//                tint = Color.Red,
//                modifier = Modifier.size(21.dp)
//            )
//            Text(
//                "Sign Out",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Red,
//                modifier = Modifier.padding(start = MaterialTheme.dimens.small1)
//            )
//        }
//    }
//}


@Composable
fun ProfileSection(
    onUserProfileClick: () -> Unit,
    onPetProfileClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.small1)
            .padding(top = MaterialTheme.dimens.small1, bottom = 6.dp),
        text = "Profiles",
        style = MaterialTheme.typography.headlineSmall,
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
                image = R.drawable.profile,
                title = "Souvik",
                description = "Personal Info",
                onClick = {
                    onUserProfileClick()
                }
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            ProfileItem(
                image = R.drawable.pet_profile,
                title = "Sheru",
                description = "Personal Info",
                onClick = {
                    onPetProfileClick()
                }
            )
        }
    }
}

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    image: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.small1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(MaterialTheme.dimens.small1.times(3)),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
        Column(modifier = Modifier.padding(start = MaterialTheme.dimens.small1)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
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
                    .size(MaterialTheme.dimens.small3)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier
                        .size(13.dp)
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
        style = MaterialTheme.typography.headlineSmall,
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
            .padding(MaterialTheme.dimens.small1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(MaterialTheme.dimens.small1.times(5).div(2)),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
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
                    .size(MaterialTheme.dimens.small3)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier
                        .size(13.dp)
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
        style = MaterialTheme.typography.headlineSmall,
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
                }
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
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.small1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(MaterialTheme.dimens.small1.times(5).div(2)),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
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
                    .size(MaterialTheme.dimens.small3)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier.size(13.dp)
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
        style = MaterialTheme.typography.headlineSmall,
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
            .padding(MaterialTheme.dimens.small1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(MaterialTheme.dimens.small1.times(5).div(2)),
            shape = CircleShape,
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null
            )
        }
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
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
                    .size(MaterialTheme.dimens.small3)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() }
                    .background(Color.White.copy(0.7f)),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}




