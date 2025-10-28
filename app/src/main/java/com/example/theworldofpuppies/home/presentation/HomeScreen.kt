package com.example.theworldofpuppies.home.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.core.domain.getImageRes
import com.example.theworldofpuppies.booking.core.domain.getScreenRoute
import com.example.theworldofpuppies.booking.core.domain.toString
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.domain.Pet
import com.example.theworldofpuppies.profile.pet.domain.PetListUiState
import com.example.theworldofpuppies.profile.pet.presentation.PetProfileCard
import com.example.theworldofpuppies.profile.pet.presentation.PetProfileViewModel
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    petListUiState: PetListUiState,
    petProfileViewModel: PetProfileViewModel
) {

    val context = LocalContext.current
    val imageList = List(6) { painterResource(id = R.drawable.pet_banner) }
    val pets = petListUiState.pets

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
            ScrollableBanner(imageList = imageList)
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
            ServiceSection(
                serviceList = Category.entries,
                context = context,
                navController = navController
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            PetProfileSection(
                pets = pets,
                navController = navController,
                petProfileViewModel = petProfileViewModel
            )

        }
    }
}

@Composable
fun PetProfileSection(
    modifier: Modifier = Modifier,
    pets: List<Pet>,
    petProfileViewModel: PetProfileViewModel,
    navController: NavController
) {
    Column(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
            text = "Your Pets",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        if (pets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = {
                        petProfileViewModel.resetPetUiState()
                        navController.navigate(Screen.PetProfileScreen.route)
                    },
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null
                    )
                }


            }
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentPadding = PaddingValues(MaterialTheme.dimens.small1),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
            ) {
                items(pets) { pet ->
                    PetProfileCard(
                        isPetListView = false,
                        isPetSelectionView = false,
                        isHomeScreenView = true,
                        pet = pet,
                        selectPet = {
                            petProfileViewModel.fillExistingPetData(pet)
                            navController.navigate(Screen.PetProfileScreen.route)
                        }
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .width(if (pets.isEmpty()) 300.dp else 100.dp)
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            onClick = {
                                petProfileViewModel.resetPetUiState()
                                navController.navigate(Screen.PetProfileScreen.route)
                            },
                            shape = CircleShape,
                            elevation = FloatingActionButtonDefaults.elevation(0.dp),
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.tertiary
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null
                            )
                        }


                    }

                }

            }

        }

    }

}

@Composable
fun ServiceSection(
    modifier: Modifier = Modifier,
    serviceList: List<Category>,
    context: Context,
    navController: NavController
) {
    Column(
        modifier = modifier
            .height(140.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
            text = "Services",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(serviceList) { service ->
                    val isVisible = remember { mutableStateOf(false) }
                    LaunchedEffect(true) { isVisible.value = true }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = if (service == serviceList.first()) MaterialTheme.dimens.small1 else 0.dp,
                                end = if (service == serviceList.last()) MaterialTheme.dimens.small1 else 0.dp
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        navController.navigate(service.getScreenRoute())
                                    },
                                painter = painterResource(service.getImageRes()),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

                        Text(
                            text = service.toString(context),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun ScrollableBanner(
    imageList: List<Painter>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.extraLarge2)
            .background(Color.Transparent),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        items(imageList) { image ->
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .padding(
                        start = MaterialTheme.dimens.small1,
                        end = if (image == imageList.last()) MaterialTheme.dimens.small1 else 0.dp
                    )
                    .clickable {},
                color = Color.White,
                shape = RoundedCornerShape(MaterialTheme.dimens.small2)
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

            }
        }
    }
}
