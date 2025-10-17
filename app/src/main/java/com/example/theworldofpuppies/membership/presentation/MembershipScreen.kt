package com.example.theworldofpuppies.membership.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.membership.domain.PremiumOption
import com.example.theworldofpuppies.refer_earn.presentation.ReferEarnScreenHeader
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val context = LocalContext.current

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    val scope = rememberCoroutineScope()

    val premiumOptions = listOf(
        PremiumOption(
            name = "Silver Plan",
            description = "Standard Membership Plan valid for 6 months.",
            price = 499.00,
            discountedPrice = 149.0,
            validity = 6
        ),
        PremiumOption(
            name = "Gold Plan",
            description = "Premium Membership Plan valid for 12 months.",
            price = 999.00,
            discountedPrice = 249.0,
            validity = 6
        )
    )

    var selectedOption by remember { mutableStateOf(premiumOptions.first()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                ReferEarnScreenHeader(
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
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch {
                            isRefreshing = true
                            delay(2000)
                            isRefreshing = false
                        }
                    },
                    state = pullToRefreshState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(
                                    RoundedCornerShape(
                                        bottomStart = 40.dp,
                                        bottomEnd = 40.dp
                                    )
                                )
                                .background(MaterialTheme.colorScheme.tertiary),
                            contentAlignment = Alignment.Center
                        ) {
                            var visible by remember { mutableStateOf(false) }

                            LaunchedEffect(Unit) {
                                visible = true
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(bottom = 16.dp, start = 20.dp),
                            ) {
                                val animationDelay = listOf(200, 400, 600, 800)

                                val texts = listOf(
                                    "10-15% OFF on all services",
                                    "Top Rated Heroes",
                                    "24x7 VIP Support",
                                    "Exclusive Benefits"
                                )

                                AnimatedVisibility(
                                    visible = visible,
                                    enter = fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 100,
                                            delayMillis = animationDelay[0]
                                        )
                                    )
                                ) {
                                    Text(
                                        text = "TheWorldOfPuppies Premium",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(vertical = 2.dp),
                                        textAlign = TextAlign.Justify
                                    )
                                }

                                texts.forEachIndexed { index, text ->
                                    AnimatedVisibility(
                                        visible = visible,
                                        enter = fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 800,
                                                delayMillis = animationDelay[index]
                                            )
                                        )
                                    ) {
                                        PremiumFeatures(
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            text = text
                                        )
                                    }
                                }
                            }
                        }


                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = 260.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    premiumOptions.forEach { option ->
                        val isSelected = option == selectedOption
                        PremiumOptionField(
                            name = option.name,
                            description = option.description,
                            price = option.price,
                            discountedPrice = option.discountedPrice,
                            isSelected = isSelected,
                            onSelect = {
                                selectedOption = option
                            }
                        )
                    }
                }

            }
        }

    }
}

@Composable
fun PremiumFeatures(modifier: Modifier = Modifier, text: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(18.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(vertical = 2.dp),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun PremiumOptionField(
    modifier: Modifier = Modifier,
    name: String?,
    description: String?,
    price: Double?,
    discountedPrice: Double?,
    isSelected: Boolean = false,
    onSelect: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = MaterialTheme.dimens.small1),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = if (isSelected) 8.dp else 0.dp,
        border = if (isSelected)
            BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary)
        else
            BorderStroke(0.dp, Color.Transparent)

    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onSelect()
                },
            color = Color.LightGray.copy(0.4f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.small1),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    name?.let {
                        Text(
                            modifier = Modifier,
                            text = it,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )

                    }
                    description?.let {
                        Text(
                            modifier = Modifier,
                            text = "Standard Membership Plan valid for 6 months.",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.W500),
                            color = MaterialTheme.colorScheme.tertiary

                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    price?.let {
                        Text(
                            modifier = Modifier,
                            text = formatCurrency(it),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.Gray,
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    discountedPrice?.let {
                        Text(
                            modifier = Modifier,
                            text = formatCurrency(discountedPrice),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )

                    }
                }

                Icon(
                    if (isSelected) Icons.Default.RadioButtonChecked else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }

}