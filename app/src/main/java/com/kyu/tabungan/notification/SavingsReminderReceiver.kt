package com.kyu.tabungan.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SavingsReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            if (SavingsReminderManager.isReminderEnabled(context)) {
                SavingsReminderManager.scheduleReminder(context)
            }
        } else {
            SavingsReminderManager.showSavingsNotification(context)
            if (SavingsReminderManager.isReminderEnabled(context)) {
                SavingsReminderManager.scheduleReminder(context)
            }
        }
    }
}
