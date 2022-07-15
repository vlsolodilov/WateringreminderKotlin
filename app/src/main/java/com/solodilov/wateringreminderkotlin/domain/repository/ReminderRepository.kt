package com.solodilov.wateringreminderkotlin.domain.repository

import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    suspend fun saveReminder(reminder: Reminder)
    suspend fun getReminderById(id: Long): Reminder
    fun getReminderList(): Flow<List<Reminder>>
    suspend fun deleteReminderById(id: Long)
    suspend fun saveAndDeleteTempReminders(id: Long)
    fun getReminderListWithPlantId(plantId: Long): Flow<List<Reminder>>
}