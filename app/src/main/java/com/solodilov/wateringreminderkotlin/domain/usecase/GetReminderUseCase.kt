package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import javax.inject.Inject

class GetReminderUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(reminderId: Long): Reminder =
        reminderRepository.getReminderById(reminderId)
}