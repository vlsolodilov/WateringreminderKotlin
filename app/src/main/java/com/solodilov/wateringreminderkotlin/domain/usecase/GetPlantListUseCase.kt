package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlantListUseCase @Inject constructor(private val plantRepository: PlantRepository) {

    operator fun invoke(): Flow<List<Plant>> =
        plantRepository.getPlantList()
}