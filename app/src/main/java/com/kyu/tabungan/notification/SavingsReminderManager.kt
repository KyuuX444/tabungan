package com.kyu.tabungan.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kyu.tabungan.MainActivity
import com.kyu.tabungan.R
import com.kyu.tabungan.data.database.TabunganDatabase
import com.kyu.tabungan.util.CurrencyFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

object SavingsReminderManager {

    private const val PREFS_NAME = "tabungan_reminder_prefs"
    private const val KEY_ENABLED = "reminder_enabled"
    private const val KEY_HOUR = "reminder_hour"
    private const val KEY_MINUTE = "reminder_minute"

    const val CHANNEL_ID = "channel_savings_reminder"
    const val NOTIFICATION_ID = 7001
    const val REMINDER_REQUEST_CODE = 7002

    const val ACTION_SAVINGS_REMINDER = "com.kyu.tabungan.ACTION_SAVINGS_REMINDER"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isReminderEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_ENABLED, true)
    }

    fun setReminderEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply()
        if (enabled) {
            scheduleReminder(context)
        } else {
            cancelReminder(context)
        }
    }

    fun getReminderHour(context: Context): Int {
        return getPrefs(context).getInt(KEY_HOUR, 20)
    }

    fun getReminderMinute(context: Context): Int {
        return getPrefs(context).getInt(KEY_MINUTE, 0)
    }

    fun setReminderTime(context: Context, hour: Int, minute: Int) {
        getPrefs(context).edit()
            .putInt(KEY_HOUR, hour)
            .putInt(KEY_MINUTE, minute)
            .apply()
        if (isReminderEnabled(context)) {
            scheduleReminder(context)
        }
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pengingat Nabung"
            val descriptionText = "Notifikasi harian pengingat untuk menabung"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.parseColor("#1687FF")
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SavingsReminderReceiver::class.java).apply {
            action = ACTION_SAVINGS_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val hour = getReminderHour(context)
        val minute = getReminderMinute(context)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SavingsReminderReceiver::class.java).apply {
            action = ACTION_SAVINGS_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun showSavingsNotification(context: Context, isTest: Boolean = false) {
        createNotificationChannel(context)

        CoroutineScope(Dispatchers.IO).launch {
            val database = TabunganDatabase.getInstance(context)
            val goals = database.savingsGoalDao().getAllGoals().firstOrNull() ?: emptyList()
            val activeGoal = goals.firstOrNull { !it.isAchieved }

            val title = if (isTest) {
                "Uji Coba Pengingat Nabung! 🔔"
            } else if (activeGoal != null) {
                "Waktunya Nabung! 🎯"
            } else {
                "Yuk Nabung Hari Ini! 💰"
            }

            val message = if (activeGoal != null) {
                if (activeGoal.dailyTarget > 0L) {
                    "Sisihkan ${CurrencyFormatter.formatRupiah(activeGoal.dailyTarget)} hari ini untuk ${activeGoal.name}."
                } else {
                    "Yuk sisihkan sedikit uangmu untuk mewujudkan target ${activeGoal.name}!"
                }
            } else {
                "Sisihkan sebagian pemasukanmu hari ini agar impian finansialmu cepat terwujud."
            }

            val deepLinkTarget = if (activeGoal != null) "tabungan://savings-goals" else "tabungan://home"

            val openIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkTarget), context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("route", deepLinkTarget)
            }
            val openPendingIntent = PendingIntent.getActivity(
                context,
                7010,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val actionIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tabungan://savings-goals"), context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("route", "tabungan://savings-goals")
            }
            val actionPendingIntent = PendingIntent.getActivity(
                context,
                7011,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.parseColor("#1687FF"))
                .setAutoCancel(true)
                .setContentIntent(openPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Lihat Target", actionPendingIntent)

            try {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
            } catch (e: SecurityException) {
            }
        }
    }

    fun sendTestNotification(context: Context) {
        showSavingsNotification(context, isTest = true)
    }
}
