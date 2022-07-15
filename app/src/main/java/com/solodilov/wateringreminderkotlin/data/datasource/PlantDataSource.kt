package com.solodilov.wateringreminderkotlin.data.datasource

import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.PlantDb
import kotlinx.coroutines.flow.Flow

interface PlantDataSource {

    suspend fun insertPlant(plantDb: PlantDb): Long
    suspend fun getPlantById(id: Long): PlantDb
    fun getPlantList(): Flow<List<PlantDb>>
    suspend fun deletePlantById(id: Long)
}