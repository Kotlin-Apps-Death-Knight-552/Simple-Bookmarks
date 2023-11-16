package com.knightshrestha.bookmarks.core.helpers

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun parseAndFormatTimestamp(timestamp: String): String {
    return OffsetDateTime.parse(timestamp)
        .atZoneSameInstant(ZoneId.of("Asia/Kathmandu"))
        .format(DateTimeFormatter.ofPattern("yyyy MMMM dd 'at' hh:mm a"))

}

fun parseDate(timestamp: String): String {
    return OffsetDateTime.parse(timestamp)
        .atZoneSameInstant(ZoneId.of("Asia/Kathmandu"))
        .format(DateTimeFormatter.ofPattern("yyyy MMMM dd"))
}
fun parseTime(timestamp: String): String {
    return OffsetDateTime.parse(timestamp)
        .atZoneSameInstant(ZoneId.of("Asia/Kathmandu"))
        .format(DateTimeFormatter.ofPattern("hh:mm a"))
}

fun parseTimeInEpoch(timestamp: String): Long {
    return OffsetDateTime.parse(timestamp)
        .atZoneSameInstant(ZoneId.of("Asia/Kathmandu"))
        .toEpochSecond()
}