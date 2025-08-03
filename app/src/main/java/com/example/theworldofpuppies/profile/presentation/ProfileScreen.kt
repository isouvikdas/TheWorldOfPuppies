package com.example.theworldofpuppies.profile.presentation

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
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
                ProfileSection()
            }
            item {
            AccountAndMembershipSection()
            }
            item {
                CommunicationAndEngagementSection()
            }
            item {
                OrderAndRewardSection()
            }
            item {
                Spacer(modifier = Modifier.padding(bottom = MaterialTheme.dimens.small1))
            }
        }
    }
}

@Composable
fun ProfileSection() {
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
                onClick = {}
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            ProfileItem(
                image = R.drawable.pet_profile,
                title = "Sheru",
                description = "Personal Info",
                onClick = {}
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
            .padding(horizontal = MaterialTheme.dimens.small1)
            .padding(vertical = MaterialTheme.dimens.small1),
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
                    modifier = Modifier.size(MaterialTheme.dimens.small1)
                )
            }
        }
    }
}

@Composable
fun AccountAndMembershipSection() {
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
                onClick = {}
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            AccountAndMembershipItem(
                image = R.drawable.address,
                title = "Address",
                onClick = {}
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
            .padding(horizontal = MaterialTheme.dimens.small1)
            .padding(vertical = MaterialTheme.dimens.small1),
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
                    modifier = Modifier.size(MaterialTheme.dimens.small1)
                )
            }
        }
    }
}

@Composable
fun CommunicationAndEngagementSection() {
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
                onClick = {}
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            CommunicationAndMembershipItem(
                image = R.drawable.refer,
                title = "Refer & Earn",
                onClick = {}
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
            .padding(horizontal = MaterialTheme.dimens.small1)
            .padding(vertical = MaterialTheme.dimens.small1),
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
                    modifier = Modifier.size(MaterialTheme.dimens.small1)
                )
            }
        }
    }
}

@Composable
fun OrderAndRewardSection() {
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
                onClick = {}
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            OrderAndRewardsItem(
                image = R.drawable.order,
                title = "Orders",
                onClick = {}
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            OrderAndRewardsItem(
                image = R.drawable.reward,
                title = "Coupons",
                onClick = {}
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
            .padding(horizontal = MaterialTheme.dimens.small1)
            .padding(vertical = MaterialTheme.dimens.small1),
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
                    modifier = Modifier.size(MaterialTheme.dimens.small1)
                )
            }
        }
    }
}


