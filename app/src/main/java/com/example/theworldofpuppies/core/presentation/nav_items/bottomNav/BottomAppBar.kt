package com.example.theworldofpuppies.core.presentation.nav_items.bottomNav

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.theworldofpuppies.home.presentation.HomeScreen
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.example.theworldofpuppies.ui.theme.dimens
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable


@SuppressLint("SuspiciousIndentation")
@Composable
fun BottomAppbar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        BottomNavigationItems.Home,
        BottomNavigationItems.Booking,
        BottomNavigationItems.Shop,
        BottomNavigationItems.Messages,
        BottomNavigationItems.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = screens.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    AnimatedNavigationBar(
        selectedIndex = selectedIndex,
        modifier = modifier
            .height(MaterialTheme.dimens.medium3)
            .padding(horizontal = MaterialTheme.dimens.small1)
            .padding(bottom = MaterialTheme.dimens.extraSmall.times(2)),
        ballAnimation = Parabolic(tween(300)),
        indentAnimation = Height(tween(300)),
        cornerRadius = shapeCornerRadius(cornerRadius = MaterialTheme.dimens.small3),
        barColor = Color.LightGray,
        ballColor = Color.Gray
    ) {
        screens.forEachIndexed { index, screen ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (currentRoute == screen.route) {
                        screen.selectedIcon(
                            Modifier.size(MaterialTheme.dimens.small1.div(4).times(5)),
                            Color.Black
                        )
                    } else {
                        screen.unselectedIcon(
                            Modifier.size(MaterialTheme.dimens.small1.div(4).times(5)),
                            Color.Black
                        )
                    }
                    screen.title?.let {
                        Text(
                            text = it,
                            fontWeight = if (currentRoute == screen.route) FontWeight.SemiBold else FontWeight.W500,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 5.dp),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}