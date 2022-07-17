package com.solodilov.wateringreminderkotlin.domain.repository

import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    suspend fun saveReminder(reminder: Reminder)
    suspend fun getReminderById(id: Long): Reminder
    suspend fun getReminderList(): List<Reminder>
    fun getReminderListWithPlantId(plantId: Long): Flow<List<Reminder>>
    suspend fun updateReminder(reminder: Reminder)
    suspend fun saveAndDeleteTempReminders(id: Long)
    suspend fun deleteReminderById(id: Long)

}