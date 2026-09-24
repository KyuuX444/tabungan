package com.kyu.tabungan

import android.app.Application
import com.kyu.tabungan.data.database.TabunganDatabase
import com.kyu.tabungan.data.repository.TabunganRepository
import com.kyu.tabungan.data.repository.TabunganRepositoryImpl
import com.kyu.tabungan.notification.SavingsReminderManager

class TabunganApp : Application() {
    lateinit var repository: TabunganRepository
        private set

    lateinit var database: TabunganDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = TabunganDatabase.getInstance(this)
        repository = TabunganRepositoryImpl(this, database)
        SavingsReminderManager.createNotificationChannel(this)
        if (SavingsReminderManager.isReminderEnabled(this)) {
            SavingsReminderManager.scheduleReminder(this)
        }
    }
}
