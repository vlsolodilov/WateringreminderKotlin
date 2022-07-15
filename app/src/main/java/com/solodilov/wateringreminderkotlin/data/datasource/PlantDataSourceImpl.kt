package com.solodilov.wateringreminderkotlin.data.datasource

import com.solodilov.wateringreminderkotlin.data.datasource.database.PlantDao
import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.PlantDb
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlantDataSourceImpl @Inject constructor(
    private val plantDAO: PlantDao,
) : PlantDataSource {

    override suspend fun insertPlant(plantDb: PlantDb): Long =
        plantDAO.insertPlant(plantDb)

    override suspend fun getPlantById(id: Long): PlantDb =
        plantDAO.getPlantById(id)

    override fun getPlantList(): Flow<List<PlantDb>> =
        plantDAO.getPlantList(Plant.TEMP_ID)

    override suspend fun deletePlantById(id: Long) =
        plantDAO.deletePlantById(id)
}