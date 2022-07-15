package com.solodilov.wateringreminderkotlin.data.repository

import com.solodilov.wateringreminderkotlin.data.datasource.ReminderDataSource
import com.solodilov.wateringreminderkotlin.data.mapper.ReminderMapper
import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val dataSource: ReminderDataSource,
    private val mapper: ReminderMapper,
) : ReminderRepository {

    override suspend fun saveReminder(reminder: Reminder) =
        dataSource.insertReminder(mapper.mapReminderToReminderDb(reminder))

    override suspend fun getReminderById(id: Long): Reminder =
        mapper.mapReminderDbToReminder(dataSource.getReminderById(id))

    override fun getReminderList(): Flow<List<Reminder>> =
        dataSource.getReminderList().map { reminderDbList ->
            reminderDbList.map { reminderDb ->
                mapper.mapReminderDbToReminder(reminderDb)
            }
        }

    override suspend fun deleteReminderById(id: Long) =
        dataSource.deleteReminderById(id)

    override suspend fun saveAndDeleteTempReminders(id: Long) =
        dataSource.saveAndDeleteTempReminders(id)

    override fun getReminderListWithPlantId(plantId: Long): Flow<List<Reminder>> =
        dataSource.getReminderListWithPlantId(plantId).map { reminderDbList ->
            reminderDbList.map { reminderDb ->
                mapper.mapReminderDbToReminder(reminderDb)
            }
        }

}