package com.example.theworldofpuppies.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val extraSmall: Dp = 0.dp,
    val small1: Dp = 0.dp,
    val small2: Dp = 0.dp,
    val small3: Dp = 0.dp,
    val medium1: Dp = 0.dp,
    val medium2: Dp = 0.dp,
    val medium3: Dp = 0.dp,
    val large1: Dp = 0.dp,
    val large2: Dp = 0.dp,
    val large3: Dp = 0.dp,
    val extraLarge1: Dp = 0.dp,
    val extraLarge2: Dp = 0.dp,
    val extraLarge3: Dp = 0.dp,
    val buttonHeight: Dp = 0.dp,
    val logoSize: Dp = 0.dp
)

val CompactSmallDimens = Dimens(
    small1 = 4.dp,
    small2 = 8.dp,
    small3 = 12.dp,
    medium1 = 16.dp,
    medium2 = 24.dp,
    medium3 = 32.dp,
    large1 = 120.dp,
    buttonHeight = 40.dp,
    logoSize = 48.dp
)

val CompactMediumDimens = Dimens(
    extraSmall = 6.dp,
    small1 = 16.dp,
    small2 = 26.dp,
    small3 = 36.dp,
    medium1 = 60.dp,
    medium2 = 70.dp,
    medium3 = 80.dp,
    large1 = 90.dp,
    large2 = 115.dp,
    large3 = 140.dp,
    extraLarge1 = 160.dp,
    extraLarge2 = 180.dp,
    extraLarge3 = 200.dp,
    buttonHeight = 44.dp,
    logoSize = 56.dp
)

val CompactDimens = Dimens(
    small1 = 8.dp,
    small2 = 12.dp,
    small3 = 16.dp,
    medium1 = 24.dp,
    medium2 = 32.dp,
    medium3 = 40.dp,
    large3 = 160.dp,
    buttonHeight = 48.dp,
    logoSize = 64.dp
)

val MediumDimens = Dimens(
    small1 = 10.dp,
    small2 = 14.dp,
    small3 = 18.dp,
    medium1 = 28.dp,
    medium2 = 36.dp,
    medium3 = 48.dp,
    large3 = 80.dp,
    buttonHeight = 52.dp,
    logoSize = 80.dp
)

val ExpandedDimens = Dimens(
    small1 = 12.dp,
    small2 = 16.dp,
    small3 = 20.dp,
    medium1 = 32.dp,
    medium2 = 40.dp,
    medium3 = 56.dp,
    large3 = 96.dp,
    buttonHeight = 56.dp,
    logoSize = 96.dp
)
