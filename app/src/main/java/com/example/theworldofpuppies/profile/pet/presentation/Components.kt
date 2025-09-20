package com.example.theworldofpuppies.profile.pet.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Circle
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.profile.pet.domain.Pet
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun PetProfileCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    isPetSelectionView: Boolean,
    selectPet: (Pet) -> Unit = {},
    isPetListView: Boolean,
    isHomeScreenView: Boolean,
    isSelected: Boolean = false,
    pet: Pet?
) {
    val horizontalPadding = MaterialTheme.dimens.small1

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = elevation
    ) {
        Surface(
            modifier = Modifier.clickable {
                pet?.let {
                    selectPet(pet)
                }
            },
            color = MaterialTheme.colorScheme.secondary.copy(0.3f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
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

                            },
                        painter = painterResource(R.drawable.pet_profile),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(start = 5.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    pet?.name?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        pet?.age?.let {
                            Text(
                                "Age: $it Months",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                        pet?.weight?.let {
                            Text(
                                "Weight: $it Kg",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                    }
                }
                when {
                    isPetListView && !isPetSelectionView && !isHomeScreenView -> {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .bounceClick {

                                    },
                                tint = MaterialTheme.colorScheme.tertiaryContainer
                            )
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .bounceClick {

                                    },
                                tint = MaterialTheme.colorScheme.errorContainer
                            )
                        }

                    }

                    isPetSelectionView && !isPetListView && !isHomeScreenView -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Icon(
                                if (isSelected) Icons.Default.CheckCircle
                                else Icons.Outlined.Circle,
                                contentDescription = "selected",
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center),
                                tint = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        }

                    }

                    isHomeScreenView && !isPetListView && !isPetSelectionView -> {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .clickable { }
                                    .background(
                                        MaterialTheme.colorScheme.tertiaryContainer.copy(
                                            0.6f
                                        )
                                    )
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center

                            ) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowForwardIos,
                                    contentDescription = "profile button",
                                    modifier = Modifier
                                        .size(11.dp)
                                )
                            }

                        }

                    }
                }
            }
        }
    }
}