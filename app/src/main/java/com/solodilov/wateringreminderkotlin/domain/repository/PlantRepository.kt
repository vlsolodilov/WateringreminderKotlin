package com.solodilov.wateringreminderkotlin.domain.repository

import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import kotlinx.coroutines.flow.Flow

interface PlantRepository {

    suspend fun savePlant(plant: Plant): Long
    suspend fun getPlantById(id: Long): Plant
    fun getPlantList(): Flow<List<Plant>>
    suspend fun deletePlantById(id: Long)
}