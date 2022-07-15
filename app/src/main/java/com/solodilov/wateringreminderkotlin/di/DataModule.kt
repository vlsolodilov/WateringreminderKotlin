package com.solodilov.wateringreminderkotlin.di

import android.app.Application
import com.solodilov.wateringreminderkotlin.data.datasource.PlantDataSource
import com.solodilov.wateringreminderkotlin.data.datasource.PlantDataSourceImpl
import com.solodilov.wateringreminderkotlin.data.datasource.ReminderDataSource
import com.solodilov.wateringreminderkotlin.data.datasource.ReminderDataSourceImpl
import com.solodilov.wateringreminderkotlin.data.datasource.database.PlantDao
import com.solodilov.wateringreminderkotlin.data.datasource.database.ReminderDao
import com.solodilov.wateringreminderkotlin.data.datasource.database.WateringReminderDatabase
import com.solodilov.wateringreminderkotlin.data.repository.PlantRepositoryImpl
import com.solodilov.wateringreminderkotlin.data.repository.ReminderRepositoryImpl
import com.solodilov.wateringreminderkotlin.domain.repository.PlantRepository
import com.solodilov.wateringreminderkotlin.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun providePlantDataSource(impl: PlantDataSourceImpl): PlantDataSource

    @Binds
    fun providePlantRepository(impl: PlantRepositoryImpl): PlantRepository

    @Binds
    fun provideReminderDataSource(impl: ReminderDataSourceImpl): ReminderDataSource

    @Binds
    fun provideReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository

    companion object {

        @Singleton
        @Provides
        fun provideWateringReminderDatabase(application: Application): WateringReminderDatabase {
            return WateringReminderDatabase.getInstance(application)
        }

        @Singleton
        @Provides
        fun providePlantDAO(wateringReminderDatabase: WateringReminderDatabase): PlantDao {
            return wateringReminderDatabase.plantDao()
        }

        @Singleton
        @Provides
        fun provideReminderDAO(wateringReminderDatabase: WateringReminderDatabase): ReminderDao {
            return wateringReminderDatabase.reminderDao()
        }
    }
}