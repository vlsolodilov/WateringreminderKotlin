package com.solodilov.wateringreminderkotlin.data.mapper

import com.solodilov.wateringreminderkotlin.data.datasource.database.entity.ReminderDb
import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import javax.inject.Inject

class ReminderMapper @Inject constructor() {

    fun mapReminderToReminderDb(reminder: Reminder): ReminderDb =
        ReminderDb(
            id = reminder.id,
            name = reminder.name,
            signalTime = reminder.signalTime,
            signalPeriod = reminder.signalPeriod,
            lastSignalDate = reminder.lastSignalDate,
            plantId = reminder.plantId,
        )

    fun mapReminderDbToReminder(reminderDb: ReminderDb): Reminder =
        Reminder(
            id = reminderDb.id,
            name = reminderDb.name,
            signalTime = reminderDb.signalTime,
            signalPeriod = reminderDb.signalPeriod,
            lastSignalDate = reminderDb.lastSignalDate,
            plantId = reminderDb.plantId,
        )

}