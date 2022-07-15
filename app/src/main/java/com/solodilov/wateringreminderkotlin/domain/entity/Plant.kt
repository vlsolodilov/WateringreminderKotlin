package com.solodilov.wateringreminderkotlin.domain.entity

import java.util.*

data class Plant(
    val id: Long = UNDEFINED_ID,
    val name: String,
    val description: String,
    val plantingDate: Date,
    val imageUri: String = UNDEFINED_IMAGE,
) {
    companion object {
        const val UNDEFINED_ID = 0L
        const val TEMP_ID = 1L
        const val UNDEFINED_IMAGE = "empty"
    }
}