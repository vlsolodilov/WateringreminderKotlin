package com.solodilov.wateringreminderkotlin.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.domain.usecase.GetReminderListUseCase
import com.solodilov.wateringreminderkotlin.presentation.notification.AlarmUtil.setAlarm
import com.solodilov.wateringreminderkotlin.ui.MainActivity
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getReminderListUseCase: GetReminderListUseCase

    companion object {
        const val NOTIFICATION_ID = "notification_id"
        const val NOTIFICATION_NAME = "reminders"
        const val NOTIFICATION_CHANNEL = "channel_reminders"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        showNotification(context, notificationId)
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            runBlocking {
                getReminderListUseCase().forEach { reminder ->
                    setAlarm(context, reminder)
                }
            }
        }

    }

    private fun showNotification(context: Context, notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_subtitle))
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(contentPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel = NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_HIGH).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                setSound(ringtoneManager, audioAttributes)
            }

            notificationManager?.createNotificationChannel(channel)
        }

        notificationManager?.notify(notificationId, notification.build())
    }

}