package com.solodilov.wateringreminderkotlin.data.repository

import com.solodilov.wateringreminderkotlin.data.datasource.PlantDataSource
import com.solodilov.wateringreminderkotlin.data.mapper.PlantMapper
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val dataSource: PlantDataSource,
    private val mapper: PlantMapper,
) : PlantRepository {

    override suspend fun savePlant(plant: Plant): Long =
        dataSource.insertPlant(mapper.mapPlantToPlantDb(plant))

    override suspend fun getPlantById(id: Long): Plant =
        mapper.mapPlantDbToPlant(dataSource.getPlantById(id))

    override fun getPlantList(): Flow<List<Plant>> =
        dataSource.getPlantList().map { plantDbList ->
            plantDbList.map { plantDB ->
                mapper.mapPlantDbToPlant(plantDB)
            }
        }

    override suspend fun updatePlant(plant: Plant) =
        dataSource.updatePlant(mapper.mapPlantToPlantDb(plant))

    override suspend fun deletePlantById(id: Long) =
        dataSource.deletePlantById(id)

}