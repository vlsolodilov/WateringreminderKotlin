package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.domain.repository.PlantRepository
import javax.inject.Inject

class GetPlantUseCase @Inject constructor(private val plantRepository: PlantRepository) {

    suspend operator fun invoke(plantId: Long): Plant =
        plantRepository.getPlantById(plantId)
}