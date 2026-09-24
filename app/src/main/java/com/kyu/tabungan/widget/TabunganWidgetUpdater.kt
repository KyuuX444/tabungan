package com.kyu.tabungan.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
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

    private fun createNavPendingIntent(context: Context, requestCode: Int, uriString: String): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString), context, MainActivity::class.java).apply {
            putExtra("route", uriString)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

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
            val savingsGoalDao = database.savingsGoalDao()

            val totalBalance = walletDao.getTotalBalance().first() ?: 0L

            val cal = Calendar.getInstance()
            val m = cal.get(Calendar.MONTH) + 1
            val y = cal.get(Calendar.YEAR)
            val monthRange = DateUtils.getMonthRange(m, y)

            val monthlyIncome = transactionDao.getIncomeSumByDateRange(monthRange.first, monthRange.second).first()
            val monthlyExpense = transactionDao.getExpenseSumByDateRange(monthRange.first, monthRange.second).first()

            val goals = savingsGoalDao.getAllGoals().first()
            val activeGoal = goals.firstOrNull { !it.isAchieved }

            val monthNames = arrayOf(
                "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
                "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
            )
            val monthLabel = "${monthNames.getOrElse(m - 1) { "" }} $y"

            if (smallIds.isNotEmpty()) {
                for (appWidgetId in smallIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_small)

                    views.setOnClickPendingIntent(R.id.widget_title_badge, createNavPendingIntent(context, 100, "tabungan://home"))
                    views.setOnClickPendingIntent(R.id.widget_chip_goal, createNavPendingIntent(context, 101, "tabungan://savings-goals"))
                    views.setOnClickPendingIntent(R.id.widget_card_balance, createNavPendingIntent(context, 102, "tabungan://wallet"))
                    views.setOnClickPendingIntent(R.id.widget_btn_income, createNavPendingIntent(context, 103, "tabungan://add-income"))
                    views.setOnClickPendingIntent(R.id.widget_btn_expense, createNavPendingIntent(context, 104, "tabungan://add-expense"))
                    views.setOnClickPendingIntent(R.id.widget_tv_subinfo, createNavPendingIntent(context, 105, "tabungan://wallet"))

                    views.setTextViewText(R.id.widget_tv_balance, CurrencyFormatter.formatRupiah(totalBalance))

                    if (activeGoal != null) {
                        val percent = if (activeGoal.targetAmount > 0L) {
                            ((activeGoal.savedAmount.toDouble() / activeGoal.targetAmount.toDouble()) * 100).toInt().coerceIn(0, 100)
                        } else 0
                        views.setTextViewText(R.id.widget_chip_goal, "🎯 $percent%")
                        views.setTextViewText(R.id.widget_tv_subinfo, "🎯 ${activeGoal.name}: $percent%")
                    } else {
                        views.setTextViewText(R.id.widget_chip_goal, "🎯 Target")
                        views.setTextViewText(R.id.widget_tv_subinfo, "Ketuk saldo untuk lihat dompet →")
                    }

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }

            if (largeIds.isNotEmpty()) {
                for (appWidgetId in largeIds) {
                    val views = RemoteViews(context.packageName, R.layout.widget_large)

                    views.setOnClickPendingIntent(R.id.widget_title_badge, createNavPendingIntent(context, 200, "tabungan://home"))
                    views.setOnClickPendingIntent(R.id.widget_chip_month, createNavPendingIntent(context, 201, "tabungan://statistics"))
                    views.setOnClickPendingIntent(R.id.widget_chip_goal, createNavPendingIntent(context, 202, "tabungan://savings-goals"))
                    views.setOnClickPendingIntent(R.id.widget_card_balance, createNavPendingIntent(context, 203, "tabungan://wallet"))
                    views.setOnClickPendingIntent(R.id.widget_card_income_box, createNavPendingIntent(context, 204, "tabungan://statistics"))
                    views.setOnClickPendingIntent(R.id.widget_card_expense_box, createNavPendingIntent(context, 205, "tabungan://statistics"))
                    views.setOnClickPendingIntent(R.id.widget_btn_income, createNavPendingIntent(context, 206, "tabungan://add-income"))
                    views.setOnClickPendingIntent(R.id.widget_btn_expense, createNavPendingIntent(context, 207, "tabungan://add-expense"))
                    views.setOnClickPendingIntent(R.id.widget_btn_goal, createNavPendingIntent(context, 208, "tabungan://savings-goals"))

                    views.setTextViewText(R.id.widget_chip_month, monthLabel)
                    views.setTextViewText(R.id.widget_tv_balance, CurrencyFormatter.formatRupiah(totalBalance))
                    views.setTextViewText(R.id.widget_tv_income, CurrencyFormatter.formatRupiah(monthlyIncome))
                    views.setTextViewText(R.id.widget_tv_expense, CurrencyFormatter.formatRupiah(monthlyExpense))

                    if (activeGoal != null) {
                        val percent = if (activeGoal.targetAmount > 0L) {
                            ((activeGoal.savedAmount.toDouble() / activeGoal.targetAmount.toDouble()) * 100).toInt().coerceIn(0, 100)
                        } else 0
                        views.setTextViewText(R.id.widget_chip_goal, "🎯 ${activeGoal.name} ($percent%)")
                    } else {
                        views.setTextViewText(R.id.widget_chip_goal, "🎯 + Target Impian")
                    }

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }
}
