package com.solodilov.wateringreminderkotlin.domain.usecase

import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReminderListWithPlantIdUseCase @Inject constructor(private val reminderRepository: ReminderRepository) {

    operator fun invoke(plantId: Long): Flow<List<Reminder>> =
        reminderRepository.getReminderListWithPlantId(plantId)
}