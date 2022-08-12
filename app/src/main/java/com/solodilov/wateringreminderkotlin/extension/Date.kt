package com.solodilov.wateringreminderkotlin.extension

import java.util.*

fun Date.addDays(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, days)

    return calendar.time
}