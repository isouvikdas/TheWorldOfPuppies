package com.example.theworldofpuppies.support.presentation

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.presentation.OrderAndRewardsItem
import com.example.theworldofpuppies.services.core.presentation.component.ServiceTopAppBar
import com.example.theworldofpuppies.support.presentation.utils.openEmailApp
import com.example.theworldofpuppies.support.presentation.utils.openPhoneDialer
import com.example.theworldofpuppies.support.presentation.utils.openWhatsappChatPreferBusiness
import com.example.theworldofpuppies.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(modifier: Modifier = Modifier, navController: NavController) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val context = LocalContext.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            SupportHeader(
                navController = navController,
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(it)
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.support_banner),
                    contentDescription = "Support",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = MaterialTheme.dimens.small1)
                )
                Spacer(modifier = Modifier.height(10.dp))
                SupportItemSection(
                    onEmailClick = {
                        openEmailApp(context, emailAddress = "souvikdas2412@gmail.com")
                    },
                    onWhatsAppClick = {
                        openWhatsappChatPreferBusiness(context = context, phoneNumber = "916009181866")
                    },
                    onPhoneClick = {
                        openPhoneDialer(context, phoneNumber = "916009181866")
                    }
                )
            }


        }
    }
}

@Composable
fun SupportItem(
    modifier: Modifier = Modifier,
    image: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable{
                onClick()
            }
            .padding(horizontal = MaterialTheme.dimens.small1, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(35.dp),
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
//            Box(
//                modifier = Modifier
//                    .size(30.dp)
//                    .clip(RoundedCornerShape(8.dp))
//                    .clickable { onClick() }
//                    .background(Color.White.copy(0.7f)),
//                contentAlignment = Alignment.Center
//
//            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "profile button",
                    modifier = Modifier.size(16.dp)
                )
//            }
        }
    }
}


@Composable
fun SupportItemSection(
    onEmailClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onPhoneClick: () -> Unit
) {

    Text(
        modifier = Modifier
            .padding(start = MaterialTheme.dimens.small1)
            .padding(top = MaterialTheme.dimens.small1, bottom = 10.dp),
        text = "How would you like to contact us?",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = MaterialTheme.dimens.small1),
        color = Color.Transparent,
        shape = RoundedCornerShape(MaterialTheme.dimens.small1),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.Start
        ) {
            SupportItem(
                image = R.drawable.email,
                title = "Email",
                onClick = {
                    onEmailClick()
                }
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            SupportItem(
                image = R.drawable.telephone,
                title = "Phone",
                onClick = {
                    onPhoneClick()
                }
            )
            HorizontalDivider(
                thickness = 0.15.dp,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1)
            )
            SupportItem(
                image = R.drawable.whatsapp,
                title = "WhatsApp",
                onClick = {
                    onWhatsAppClick()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "Support"
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