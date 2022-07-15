package com.solodilov.wateringreminderkotlin.data.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.PlantDb
import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.ReminderDb
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(
    entities = [PlantDb::class, ReminderDb::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class
)
abstract class WateringReminderDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun reminderDao(): ReminderDao

    companion object {

        @Volatile
        private var INSTANCE: WateringReminderDatabase? = null

        fun getInstance(context: Context): WateringReminderDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        WateringReminderDatabase::class.java,
                        "watering_reminder_database"
                    )
                        .addCallback(WateringReminderDatabaseCallback())
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private class WateringReminderDatabaseCallback() : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.plantDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(plantDao: PlantDao) {
            plantDao.insertPlant(PlantDb(Plant.TEMP_ID,"", "", Date(), ""))
        }
    }
}