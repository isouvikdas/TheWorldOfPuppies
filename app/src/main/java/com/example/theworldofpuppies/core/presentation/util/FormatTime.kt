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

fun formatDateTime(
    date: LocalDateTime,
    pattern: String = "EEEE dd MMM yyyy",
    zone: ZoneId? = ZoneId.systemDefault()
): String = date.atZone(zone).format(DateTimeFormatter.ofPattern(pattern))

fun formatDayOfWeek(date: LocalDate): String =
    date.format(DateTimeFormatter.ofPattern("EEEEE")) // Mon, Tue…

fun formatDayOfMonth(date: LocalDate): String =
    date.format(DateTimeFormatter.ofPattern("dd")) // 23, 24…

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()

fun Long.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this).atZone(zoneId).toLocalDateTime()

fun LocalDate.toEpochMillis(): Long =
    this.atStartOfDay(zoneId).toInstant().toEpochMilli()

fun Long.toLocalTime(): LocalTime =
    Instant.ofEpochMilli(this).atZone(zoneId).toLocalTime()

fun LocalDateTime.toEpochMillis(zone: ZoneId = ZoneId.systemDefault()): Long {
    return this.atZone(zone).toInstant().toEpochMilli()
}

fun LocalDateTime.atTimeEpochMillis(time: LocalTime, zone: ZoneId = ZoneId.systemDefault()): Long {
    return this.toLocalDate().atTime(time).toEpochMillis(zone)
}
