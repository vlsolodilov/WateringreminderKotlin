package com.solodilov.wateringreminderkotlin.data.datasource

import com.solodilov.wateringreminderkotlin.data.datasource.database.ReminderDao
import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.ReminderDb
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderDataSourceImpl @Inject constructor(
    private val reminderDAO: ReminderDao,
) : ReminderDataSource {

    override suspend fun insertReminder(reminderDb: ReminderDb) =
        reminderDAO.insertReminder(reminderDb)

    override suspend fun getReminderById(id: Long): ReminderDb =
        reminderDAO.getReminderById(id)

    override suspend fun getReminderList(): List<ReminderDb> =
        reminderDAO.getReminderList()

    override fun getReminderListWithPlantId(plantId: Long): Flow<List<ReminderDb>> =
        reminderDAO.getReminderListWithPlantId(plantId)

    override suspend fun saveAndDeleteTempReminders(plantId: Long) =
        reminderDAO.saveAndDeleteTempReminders(plantId, Plant.TEMP_ID)

    override suspend fun updateReminder(reminderDb: ReminderDb) =
        reminderDAO.updateReminder(reminderDb)

    override suspend fun deleteReminderById(id: Long) =
        reminderDAO.deleteReminderById(id)

}