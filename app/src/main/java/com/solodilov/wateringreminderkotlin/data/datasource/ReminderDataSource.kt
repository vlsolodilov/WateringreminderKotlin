package com.solodilov.wateringreminderkotlin.data.datasource

import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.ReminderDb
import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {

    suspend fun insertReminder(reminderDb: ReminderDb)
    suspend fun getReminderById(id: Long): ReminderDb
    suspend fun getReminderList(): List<ReminderDb>
    fun getReminderListWithPlantId(plantId: Long): Flow<List<ReminderDb>>
    suspend fun saveAndDeleteTempReminders(plantId: Long)
    suspend fun updateReminder(reminderDb: ReminderDb)
    suspend fun deleteReminderById(id: Long)

}