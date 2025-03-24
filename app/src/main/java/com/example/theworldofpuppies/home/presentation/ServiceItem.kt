package com.example.theworldofpuppies.home.presentation

import androidx.compose.foundation.Image
import com.example.theworldofpuppies.core.domain.Service
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.domain.Plan
import com.example.theworldofpuppies.ui.theme.AppTheme

@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    service: Service,
    onServiceSelect: () -> Unit
) {
    ElevatedCard(
        onClick = { onServiceSelect() },
        modifier = modifier
            .wrapContentHeight()
            .width(165.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        colors = CardDefaults.elevatedCardColors(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
            ) {
                if (service.imageUri == null) {
                    Image(
                        painter = painterResource(id = R.drawable.login),
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    AsyncImage(
                        model = service.imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                    )


                }
                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = service.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(10.dp))

            }

        }
    }
}


@Preview
@Composable
private fun ServiceItemPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ServiceItem(service = Service(
                localId = 1,
                id = "",
                name = "Pet Grooming",
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
            )) {

            }

        }
    }
}
