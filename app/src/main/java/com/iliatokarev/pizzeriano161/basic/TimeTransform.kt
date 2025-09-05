package com.iliatokarev.pizzeriano161.basic

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtilsForOrder {
    private val formatter = DateTimeFormatter.ofPattern("HH-mm dd-MM-yyyy")

    fun longToString(timeMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): String {
        val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), zoneId)
        return dateTime.format(formatter)
    }

    fun stringToLong(timeString: String, zoneId: ZoneId = ZoneId.systemDefault()): Long {
        val dateTime = LocalDateTime.parse(timeString, formatter)
        return dateTime.atZone(zoneId).toInstant().toEpochMilli()
    }
}