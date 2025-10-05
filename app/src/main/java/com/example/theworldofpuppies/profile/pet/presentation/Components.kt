package com.example.theworldofpuppies.profile.pet.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.core.presentation.util.isSmallScreenHeight
import com.example.theworldofpuppies.profile.pet.domain.Pet
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun PetProfileCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    isPetSelectionView: Boolean,
    selectPet: (Pet) -> Unit = {},
    isPetListView: Boolean,
    isHomeScreenView: Boolean,
    isSelected: Boolean = false,
    pet: Pet?,
    onDeleteClick: (Pet) -> Unit = {},
    selectPetForService: (Pet) -> Unit = {}
) {
    val horizontalPadding = MaterialTheme.dimens.small1
    val showDeleteDialog = remember { mutableStateOf(false) }
    if (showDeleteDialog.value) {
        DeleteDialog(
            onDismiss = { showDeleteDialog.value = false },
            onDeleteConfirm = {
                pet?.let {
                    onDeleteClick(it)
                }
            }
        )
    }

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
                    if (isPetSelectionView) {
                        selectPetForService(pet)
                    } else {
                        selectPet(pet)
                    }
                }
            },
            color = Color.LightGray.copy(0.4f),
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
                    color = MaterialTheme.colorScheme.secondary,
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .padding(1.dp)
                            .size(70.dp)
                            .clip(CircleShape),
                        model = pet?.downloadUrl?.toUri(),
                        contentDescription = "Pet Profile Pic",
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.pet_profile)
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
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp)
                                    .bounceClick {
                                        showDeleteDialog.value = true
                                    },
                                tint = MaterialTheme.colorScheme.errorContainer
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
                                            alpha = 1f
                                        )
                                    )
                                    .align(Alignment.Center),
                                contentAlignment = Alignment.Center

                            ) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowForwardIos,
                                    contentDescription = "profile button",
                                    modifier = Modifier
                                        .size(11.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                        }

                    }

                    else -> {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                if (isSelected) Icons.Default.CheckCircle
                                else Icons.Outlined.Circle,
                                contentDescription = "selected",
                                modifier = Modifier
                                    .size(22.dp),
                                tint = MaterialTheme.colorScheme.tertiaryContainer
                            )

                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "selected",
                                modifier = Modifier
                                    .size(22.dp)
                                    .bounceClick {
                                        pet?.let {
                                            selectPet(pet)
                                        }
                                    },
                                tint = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        }

                    }

                }
            }
        }
    }
}

@Composable
fun DeleteDialog(
    modifier: Modifier = Modifier,
    onDeleteConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .shadow(elevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(if (isSmallScreenHeight()) 6.dp else 12.dp))

                    Image(
                        painterResource(R.drawable.warning),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.height(if (isSmallScreenHeight()) 6.dp else 12.dp))

                    Text(
                        text = "Are you sure you want to delete?",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W500
                    )

                    Spacer(modifier = Modifier.height(if (isSmallScreenHeight()) 15.dp else 30.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {

                        OutlinedButton(
                            onClick = {
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .bounceClick {}
                                .padding(end = 5.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = "Cancel",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(vertical = 1.dp),
                                color = MaterialTheme.colorScheme.errorContainer
                            )
                        }

                        Button(
                            onClick = {
                                onDeleteConfirm()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .bounceClick {}
                                .padding(start = 5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }

                    }


                }
            }
        }
    }
}
