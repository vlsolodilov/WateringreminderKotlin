package com.solodilov.wateringreminderkotlin.domain.entity

import java.util.*

data class Reminder(
    val id: Long = UNDEFINED_ID,
    val name: String,
    val signalTime: Date,
    val signalPeriod: Int,
    val lastSignalDate: Date,
    val plantId: Long,
) {
    companion object {
        const val UNDEFINED_ID = 0L
    }
}