package com.example.theworldofpuppies.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.domain.Plan
import com.example.theworldofpuppies.core.domain.Service
import com.example.theworldofpuppies.ui.theme.AppTheme
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val imageList = List(6) { painterResource(id = R.drawable.pet_banner) }

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
            ServiceSection(serviceList = serviceList)
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            PetExpertSection()
        }
    }
}

@Composable
fun PetExpertSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(MaterialTheme.dimens.extraLarge3)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = MaterialTheme.dimens.small1),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Nearby Veterinary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = stringResource(R.string.see_all),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.fillMaxHeight(.04f))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = PaddingValues(MaterialTheme.dimens.small1),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
        ) {
            items(serviceList.take(4)) {
                PetExpertItem()
            }

        }

    }

}


@Composable
fun PetExpertItem(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(MaterialTheme.dimens.small2),
        color = Color.White,
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary.copy(0.3f))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(MaterialTheme.dimens.large2),
                shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                color = MaterialTheme.colorScheme.primary.copy(0.9f)

            ) {
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = MaterialTheme.dimens.small1)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Dr. Kevin Julio",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Veterinary Dentist",
                        fontWeight = FontWeight.W400,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray,
                    )

                }
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Ratings",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(MaterialTheme.dimens.small2)
                        )
                        Text(
                            text = "4.7 ",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(MaterialTheme.dimens.small2)
                        )
                        Text(
                            text = "1.5 km",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.small3)
                        .clip(CircleShape)
                        .clickable { }
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center

                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.dimens.small1),
                        tint = Color.White
                    )
                }
            }
        }
    }

}


@Composable
fun ServiceSection(modifier: Modifier = Modifier, serviceList: List<Service>) {
    Column(
        modifier = modifier
            .height(MaterialTheme.dimens.large3)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = MaterialTheme.dimens.small1),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Services",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.see_all),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                modifier = Modifier.clickable { }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                items(serviceList) { service ->
                    val isVisible = remember { mutableStateOf(false) }
                    LaunchedEffect(true) { isVisible.value = true }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = MaterialTheme.dimens.small1,
                                end = if (service == serviceList.last()) MaterialTheme.dimens.small1 else 0.dp
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(MaterialTheme.dimens.medium2)
                                .clip(CircleShape),
                            color = Color.Gray.copy(0.3f)

                        ) {
                            // Put your Image/Icon here
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.W500
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


@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            HomeScreen()
        }
    }
}

val serviceList = listOf(
    Service(
        localId = 1,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 2,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 3,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 4,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 5,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 6,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 7,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 8,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 9,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    ),
    Service(
        localId = 10,
        id = "",
        name = "Grooming",
        category = "Dog",
        plans = List(5) {
            Plan(
                name = "Spa Bath",
                price = 800.0,
                description = "Both with Shampoo and Conditioner"
            )
        },
        imageId = "",
        imageUri = ""
    )


)

