package com.solodilov.wateringreminderkotlin.presentation.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.extension.addDays
import java.util.*

object AlarmUtil {

    fun setAlarm(context: Context, reminder: Reminder) {
        val notificationId = reminder.id.toInt()
        val startTime =
            reminder.lastSignalDate.addDays(reminder.signalPeriod).time +
                    reminder.signalTime.time +
                    TimeZone.getDefault().getOffset(System.currentTimeMillis())
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            startTime,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, notificationId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            notifyIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}