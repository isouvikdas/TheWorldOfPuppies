package com.example.theworldofpuppies.shop.order.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun OrderSuccessDialog(
    modifier: Modifier = Modifier,
    orderId: String,
    onViewOrder: () -> Unit,
    onContinueShopping: () -> Unit,
    onDismiss: () -> Unit
) {
    // Backdrop dim + scale/fade animation
    Dialog(onDismissRequest = onDismiss) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.9f, animationSpec = tween(300)),
            exit = fadeOut(tween(200)) + scaleOut(tween(200))
        ) {
            Card(
                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                modifier = modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            vertical = MaterialTheme.dimens.small1,
                            horizontal = MaterialTheme.dimens.extraSmall
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.checked),
                        contentDescription = "checked",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Congratulations!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text("🎉", style = MaterialTheme.typography.displaySmall)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Your order has been successfully placed.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Order ID: $orderId",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500

                    )
                    Spacer(Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = onViewOrder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.medium1),
                            shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.secondary,
                                disabledContainerColor = Color.LightGray,
                                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        ) {
                            Text(
                                "Continue Shopping",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                        OutlinedButton(
                            onClick = onContinueShopping,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.medium1),
                            shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.tertiary,
                                disabledContainerColor = Color.LightGray,
                                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ),
                            border = BorderStroke(
                                1.3.dp,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(
                                "View Order",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
