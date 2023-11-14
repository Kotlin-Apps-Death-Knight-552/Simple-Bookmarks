package com.knightshrestha.bookmarks.helpers

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun parseAndFormatTimestamp(timestamp: String): String {
    return OffsetDateTime.parse(timestamp)
        .atZoneSameInstant(ZoneId.of("Asia/Kathmandu"))
        .format(DateTimeFormatter.ofPattern("yyyy MMMM dd 'at' hh:mm a"))

}