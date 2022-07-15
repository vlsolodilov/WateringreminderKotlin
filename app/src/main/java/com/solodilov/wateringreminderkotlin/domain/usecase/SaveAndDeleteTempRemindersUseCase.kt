package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import javax.inject.Inject

class SaveAndDeleteTempRemindersUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(plantId: Long) =
        reminderRepository.saveAndDeleteTempReminders(plantId)
}