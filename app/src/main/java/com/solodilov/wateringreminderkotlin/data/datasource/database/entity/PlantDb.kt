package com.solodilov.wateringreminderkotlin.data.datasource.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "plant_table")
data class PlantDb(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "planting_date")
    val plantingDate: Date,

    @ColumnInfo(name = "image_uri")
    val imageUri: String,
) : Serializable