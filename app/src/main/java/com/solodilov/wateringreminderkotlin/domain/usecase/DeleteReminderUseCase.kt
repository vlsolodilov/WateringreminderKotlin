package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.repository.PlantRepository
import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(id: Long) =
        reminderRepository.deleteReminderById(id)
}