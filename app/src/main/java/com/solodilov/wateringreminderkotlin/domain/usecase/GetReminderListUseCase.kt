package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReminderListUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(): List<Reminder> =
        reminderRepository.getReminderList()
}