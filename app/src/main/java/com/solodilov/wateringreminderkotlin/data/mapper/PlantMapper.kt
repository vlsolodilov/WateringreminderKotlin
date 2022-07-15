package com.solodilov.wateringreminderkotlin.data.mapper

import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.PlantDb
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import javax.inject.Inject

class PlantMapper @Inject constructor() {

    fun mapPlantToPlantDb(plant: Plant): PlantDb =
        PlantDb(
            id = plant.id,
            name = plant.name,
            description = plant.description,
            plantingDate = plant.plantingDate,
            imageUri = plant.imageUri,
        )

    fun mapPlantDbToPlant(plantDb: PlantDb): Plant =
        Plant(
            id = plantDb.id,
            name = plantDb.name,
            description = plantDb.description,
            plantingDate = plantDb.plantingDate,
            imageUri = plantDb.imageUri,
        )

}