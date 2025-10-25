package com.example.theworldofpuppies.booking.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.theworldofpuppies.core.presentation.util.formatCurrency
import com.example.theworldofpuppies.ui.theme.dimens

@Composable
fun WalletBalanceRow(
    modifier: Modifier = Modifier,
    priceTitleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    priceTitleWeight: FontWeight = FontWeight.W500,
    priceStyle: TextStyle = MaterialTheme.typography.titleMedium,
    priceWeight: FontWeight = FontWeight.SemiBold,
    walletBalance: Double
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.dimens.small1,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Wallet Balance",
            style = priceTitleStyle,
            fontWeight = priceTitleWeight
        )

        Text(
            "-${formatCurrency(walletBalance)}",
            style = priceStyle,
            fontWeight = priceWeight,
        )
    }

}