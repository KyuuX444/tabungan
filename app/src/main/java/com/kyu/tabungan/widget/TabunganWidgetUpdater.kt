package com.kyu.tabungan.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.kyu.tabungan.MainActivity
import com.kyu.tabungan.R
import com.kyu.tabungan.data.database.TabunganDatabase
import com.kyu.tabungan.util.CurrencyFormatter
import com.kyu.tabungan.util.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

object TabunganWidgetUpdater {

    fun updateAll(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val smallComponent = ComponentName(context, TabunganWidgetSmallProvider::class.java)
        val largeComponent = ComponentName(context, TabunganWidgetLargeProvider::class.java)

        val smallIds = appWidgetManager.getAppWidgetIds(smallComponent)
        val largeIds = appWidgetManager.getAppWidgetIds(largeComponent)

        if (smallIds.isEmpty() && largeIds.isEmpty()) return

        CoroutineScope(Dispatchers.IO).launch {
            val database = TabunganDatabase.getInstance(context)
            val walletDao = database.walletDao()
            val transactionDao = database.transactionDao()

            val totalBalance = walletDao.getTotalBalance().first() ?: 0L

            val cal = Calendar.getInstance()
            val m = cal.get(Calendar.MONTH) + 1
            val y = cal.get(Calendar.YEAR)
            val monthRange = DateUtils.getMonthRange(m, y)

            val monthlyIncome = transactionDao.getIncomeSumByDateRange(monthRange.first, monthRange.second).first()
            val monthlyExpense = transactionDao.getExpenseSumByDateRange(monthRange.first, monthRange.second).first()
            val totalTransactions = transactionDao.getAllTransactionsSync().size

            val isDataEmpty = totalTransactions == 0 && totalBalance == 0L

            if (smallIds.isNotEmpty()) {
                for (appWidgetId in smallIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_small)

                    val openAppIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    val openAppPendingIntent = PendingIntent.getActivity(
                        context,
                        101,
                        openAppIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_content_layout, openAppPendingIntent)

                    val addExpenseIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tabungan://add-expense"), context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    val addExpensePendingIntent = PendingIntent.getActivity(
                        context,
                        102,
                        addExpenseIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_btn_add, addExpensePendingIntent)

                    if (isDataEmpty) {
                        views.setViewVisibility(R.id.widget_label_balance, View.GONE)
                        views.setTextViewText(R.id.widget_tv_balance, "Belum ada data")
                        views.setViewVisibility(R.id.widget_tv_empty, View.VISIBLE)
                    } else {
                        views.setViewVisibility(R.id.widget_label_balance, View.VISIBLE)
                        views.setTextViewText(R.id.widget_tv_balance, CurrencyFormatter.formatRupiah(totalBalance))
                        views.setViewVisibility(R.id.widget_tv_empty, View.GONE)
                    }

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }

            if (largeIds.isNotEmpty()) {
                for (appWidgetId in largeIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_large)

                    val openAppIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    val openAppPendingIntent = PendingIntent.getActivity(
                        context,
                        201,
                        openAppIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_content_layout, openAppPendingIntent)

                    val addIncomeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tabungan://add-income"), context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    val addIncomePendingIntent = PendingIntent.getActivity(
                        context,
                        202,
                        addIncomeIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_btn_income, addIncomePendingIntent)

                    val addExpenseIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tabungan://add-expense"), context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    val addExpensePendingIntent = PendingIntent.getActivity(
                        context,
                        203,
                        addExpenseIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_btn_expense, addExpensePendingIntent)

                    if (isDataEmpty) {
                        views.setViewVisibility(R.id.widget_label_balance, View.GONE)
                        views.setTextViewText(R.id.widget_tv_balance, "Belum ada data")
                        views.setViewVisibility(R.id.widget_tv_empty, View.VISIBLE)
                        views.setViewVisibility(R.id.widget_stats_container, View.GONE)
                    } else {
                        views.setViewVisibility(R.id.widget_label_balance, View.VISIBLE)
                        views.setTextViewText(R.id.widget_tv_balance, CurrencyFormatter.formatRupiah(totalBalance))
                        views.setViewVisibility(R.id.widget_tv_empty, View.GONE)
                        views.setViewVisibility(R.id.widget_stats_container, View.VISIBLE)
                        views.setTextViewText(R.id.widget_tv_income, CurrencyFormatter.formatRupiah(monthlyIncome))
                        views.setTextViewText(R.id.widget_tv_expense, CurrencyFormatter.formatRupiah(monthlyExpense))
                    }

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }
}
