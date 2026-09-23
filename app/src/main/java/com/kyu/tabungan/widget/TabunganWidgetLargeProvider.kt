package com.kyu.tabungan.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

class TabunganWidgetLargeProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        TabunganWidgetUpdater.updateAll(context)
    }

    override fun onEnabled(context: Context) {
        TabunganWidgetUpdater.updateAll(context)
    }
}
