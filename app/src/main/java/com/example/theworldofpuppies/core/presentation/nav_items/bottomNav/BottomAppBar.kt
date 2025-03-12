package com.example.theworldofpuppies.core.presentation.nav_items.bottomNav

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.theworldofpuppies.home.presentation.HomeScreen
import com.example.theworldofpuppies.ui.theme.AppTheme

@Composable
fun BottomAppbar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        BottomNavigationItems.Home,
        BottomNavigationItems.Booking,
        BottomNavigationItems.Messages,
        BottomNavigationItems.Profile
    )
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(bottom = 5.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBar(
            modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            containerColor = Color.White,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (currentRoute == screen.route) screen.selectedIcon!! else screen.unselectedIcon!!,
                            contentDescription = screen.title,
                            modifier = Modifier.size(25.dp),
                            tint = if (currentRoute == screen.route) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(
                            text = screen.title.toString(),
                            color = if (currentRoute == screen.route) MaterialTheme.colorScheme.primary else Color.Gray,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                    alwaysShowLabel = true
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomAppBarPreview() {
    AppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(0.2f)),
            bottomBar = {
                BottomAppbar(
                    navController = rememberNavController()
                )
            }
        ) { innerPadding ->
            HomeScreen(
                modifier = Modifier
                    .padding(innerPadding)
            )
        }

    }
}