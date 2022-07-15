package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import javax.inject.Inject

class SaveReminderUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) =
        reminderRepository.saveReminder(reminder)
}