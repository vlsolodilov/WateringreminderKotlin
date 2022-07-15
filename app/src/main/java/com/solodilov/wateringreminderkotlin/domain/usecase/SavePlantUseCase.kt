package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.domain.repository.PlantRepository
import javax.inject.Inject

class SavePlantUseCase @Inject constructor(private val plantRepository: PlantRepository) {

    suspend operator fun invoke(plant: Plant): Long =
        plantRepository.savePlant(plant)
}