package com.example.theworldofpuppies.core.presentation.nav_items.topNav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopAppbar(
    navController: NavHostController,
    modifier: Modifier,
    profileButtonVisibility: Boolean,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    Box(
        modifier = modifier
            .fillMaxHeight(0.074f)
            .shadow(1.dp, shape = RoundedCornerShape(0.dp)) // Add elevation here
    ) {
        NavigationBar(
            containerColor = Color.White,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.furryroyals_high_resolution_logo_transparent5),
//                    contentDescription = "Logo",
//                    modifier = Modifier
//                        .height(80.dp)
//                        .width(130.dp)
//                        .align(Alignment.CenterVertically)
//                )

                IconButton(onClick = {
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Default.Notes, contentDescription = "Menu")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (profileButtonVisibility) {
                    BottomNavigationItems.Profile?.let {
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = it.selectedIcon!!,
                                    contentDescription = "Profile"
                                )
                            },
                            selected = currentRoute == it.route,
                            onClick = {
                                if (currentRoute != it.route) { // Prevent redundant navigation
                                    navController.navigate(it.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun TopAppBarPreview() {
    AppTheme {
        val topBarVisible by remember { mutableStateOf(true) }
        val navController: NavHostController = rememberNavController()
        Scaffold(
            topBar = {
                if (topBarVisible) {
                    TopAppbar(
                        navController,
                        modifier = Modifier,
                        profileButtonVisibility = true,
                        scope = rememberCoroutineScope(),
                        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues))
        }
    }
}
