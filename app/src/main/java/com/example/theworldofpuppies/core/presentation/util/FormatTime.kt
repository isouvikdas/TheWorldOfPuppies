package com.example.theworldofpuppies.core.presentation.util

import android.view.autofill.AutofillValue.forDate
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


val zoneId: ZoneId? = ZoneId.systemDefault()
fun formatEpochMillis(epochMillis: Long, pattern: String = "dd MMM yyyy"): String {
    val instant = Instant.ofEpochMilli(epochMillis)
    val zonedDateTime = instant.atZone(zoneId)

    val formatter = DateTimeFormatter.ofPattern(pattern)
    return zonedDateTime.format(formatter)
}

fun formatDayOfWeek(date: LocalDate): String =
    date.format(DateTimeFormatter.ofPattern("EEEEE")) // Mon, Tue…

fun formatDayOfMonth(date: LocalDate): String =
    date.format(DateTimeFormatter.ofPattern("dd")) // 23, 24…

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

fun Long.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDateTime()

fun LocalDate.toEpochMillis(): Long =
    this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

fun Long.toLocalTime(): LocalTime =
    Instant.ofEpochMilli(this).atZone(zoneId).toLocalTime()

