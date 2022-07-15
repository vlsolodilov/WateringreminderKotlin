package com.solodilov.wateringreminderkotlin.data.datasource.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "reminder_table",
    foreignKeys = [ForeignKey(
        entity = PlantDb::class,
        parentColumns = ["id"],
        childColumns = ["plant_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReminderDb(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "signal_time")
    val signalTime: Date,

    @ColumnInfo(name = "signal_period")
    val signalPeriod: Int,

    @ColumnInfo(name = "last_signal_time")
    val lastSignalDate: Date,

    @ColumnInfo(name = "plant_id")
    val plantId: Long,
) : Serializable