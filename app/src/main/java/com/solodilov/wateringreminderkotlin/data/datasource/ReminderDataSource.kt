package com.solodilov.wateringreminderkotlin.data.datasource

import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.ReminderDb
import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {

    suspend fun insertReminder(reminderDb: ReminderDb)
    suspend fun getReminderById(id: Long): ReminderDb
    fun getReminderList(): Flow<List<ReminderDb>>
    fun getReminderListWithPlantId(plantId: Long): Flow<List<ReminderDb>>
    suspend fun deleteReminderById(id: Long)
    suspend fun saveAndDeleteTempReminders(plantId: Long)
}