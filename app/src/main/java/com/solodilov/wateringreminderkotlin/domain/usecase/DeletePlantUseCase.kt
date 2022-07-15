package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.repository.PlantRepository
import javax.inject.Inject

class DeletePlantUseCase @Inject constructor(private val plantRepository: PlantRepository) {

    suspend operator fun invoke(id: Long) =
        plantRepository.deletePlantById(id)
}