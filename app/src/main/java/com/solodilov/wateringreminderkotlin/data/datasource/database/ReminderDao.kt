package com.solodilov.wateringreminderkotlin.data.datasource.database

import androidx.room.*
import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.ReminderDb
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertReminder(reminderDb: ReminderDb)

    @Query("SELECT * FROM reminder_table WHERE id =:id  ")
    abstract suspend fun getReminderById(id: Long): ReminderDb

    @Query("SELECT * FROM reminder_table")
    abstract suspend fun getReminderList(): List<ReminderDb>

    @Query("SELECT * FROM reminder_table WHERE plant_id =:plantId")
    abstract fun getReminderListWithPlantId(plantId: Long): Flow<List<ReminderDb>>

    @Query("UPDATE reminder_table SET plant_id =:plantId WHERE plant_id =:tempPlantId")
    abstract suspend fun saveTempReminders(plantId: Long, tempPlantId: Long)

    @Transaction
    open suspend fun saveAndDeleteTempReminders(plantId: Long, tempPlantId: Long) {
        saveTempReminders(plantId, tempPlantId)
        deleteTempReminders(tempPlantId)
    }

    @Update
    abstract suspend fun updateReminder(reminderDb: ReminderDb)

    @Query("DELETE FROM reminder_table WHERE id =:id")
    abstract suspend fun deleteReminderById(id: Long)

    @Query("DELETE FROM reminder_table WHERE id =:tempPlantId")
    abstract suspend fun deleteTempReminders(tempPlantId: Long)



}