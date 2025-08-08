package com.example.theworldofpuppies.core.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatEpochMillis(epochMillis: Long): String {
    val instant = Instant.ofEpochMilli(epochMillis)
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return zonedDateTime.format(formatter)
}