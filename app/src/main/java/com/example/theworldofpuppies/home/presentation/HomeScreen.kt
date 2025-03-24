package com.example.theworldofpuppies.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.domain.Plan
import com.example.theworldofpuppies.core.domain.Service
import com.example.theworldofpuppies.ui.theme.AppTheme


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val lazyGridState = rememberLazyGridState()
    val imageList = List(10) { painterResource(id = R.drawable.flag_india) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            ScrollableBanner(imageList = imageList)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            CategorySection(serviceList = serviceList)

        }
    }
}

@Composable
fun CategorySection(modifier: Modifier = Modifier, serviceList: List<Service>) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardSize = (screenWidth / 5).coerceAtMost(100.dp)
    val verticalPadding = (cardSize * 0.1f).coerceAtLeast(4.dp)

    Column(
        modifier = modifier
            .fillMaxHeight(0.4f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "See all",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                modifier = Modifier.clickable { }
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.07f))

        LazyRow {
            items(serviceList) { service ->
                val isVisible = remember { mutableStateOf(false) }
                LaunchedEffect(true) { isVisible.value = true }

                AnimatedVisibility(
                    visible = isVisible.value,
                    enter = fadeIn() + expandVertically()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .padding(end = if (service == serviceList.last()) 20.dp else 0.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .size(cardSize)
                                    .clip(CircleShape),
                                colors = CardColors(
                                    containerColor = Color.LightGray.copy(0.4f),
                                    contentColor = Color.White,
                                    disabledContentColor = Color.Gray,
                                    disabledContainerColor = Color.White
                                )
                            ) {

                            }
                        }

                        Spacer(modifier = Modifier.height(verticalPadding))

                        Text(
                            text = service.name,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = 20.dp)
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
            .fillMaxHeight(0.27f),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        items(imageList) { image ->
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = if (image == imageList.last()) 20.dp else 0.dp
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .shadow(elevation = 10.dp)
                    .clickable {}
            )
        }
    }
}

@Composable
fun StaticBanner(modifier: Modifier = Modifier, image: Painter) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.24f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { },
        contentScale = ContentScale.Crop
    )
}


@Composable
fun ServiceSection(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    serviceList: List<Service>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Services",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = lazyGridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(serviceList) { service ->
                ServiceItem(service = service) {

                }
            }
        }
    }
}

//@Preview
//@Composable
//private fun ServiceSectionPreview() {
//    AppTheme {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Gray)
//        ) {
//            Spacer(
//                modifier = Modifier
//                    .fillMaxHeight(0.025f)
//                    .background(Color.White)
//            )
//            ServiceSection(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.5f)
//                    .background(
//                        Color.White
//                    ),
//                lazyGridState = rememberLazyGridState(),
//                serviceList = List<Service>
//            )
//
//        }
//    }
//}

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

