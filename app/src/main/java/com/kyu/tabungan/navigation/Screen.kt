package com.kyu.tabungan.navigation

import com.kyu.tabungan.data.entity.TransactionType

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Statistics : Screen("statistics")
    object AddTransaction : Screen("add_transaction?transactionId={transactionId}&type={type}") {
        fun createRoute(transactionId: Long? = null, type: TransactionType? = null): String {
            val idParam = if (transactionId != null && transactionId > 0) "transactionId=$transactionId" else ""
            val typeParam = if (type != null) "type=${type.name}" else ""
            val params = listOf(idParam, typeParam).filter { it.isNotEmpty() }.joinToString("&")
            return if (params.isNotEmpty()) "add_transaction?$params" else "add_transaction"
        }
    }
    object TransactionDetail : Screen("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: Long): String = "transaction_detail/$transactionId"
    }
    object Wallets : Screen("wallets")
    object Budgets : Screen("budgets")
    object Categories : Screen("categories")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object More : Screen("more")
    object AboutDev : Screen("about_dev")
}
