package com.solodilov.wateringreminderkotlin.extension

import java.text.SimpleDateFormat
import java.util.*

object DateTimeConverter {

    private val locale = Locale.getDefault()
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", locale)
    private val timeFormatter = SimpleDateFormat("HH:mm", locale)

    fun getFormattedDate(date: Date): String =
        dateFormatter.format(date)

    fun getFormattedTime(time: Date): String =
        timeFormatter.format(time)

    fun getTime(hour: Int, minute: Int): String =
        "%02d:%02d".format(hour, minute)

    fun getDate(date: String): Date =
        dateFormatter.parse(date)

    fun getTime(time: String): Date =
        timeFormatter.parse(time)

}