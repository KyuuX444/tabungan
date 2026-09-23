package com.kyu.tabungan.data.backup

import com.google.gson.annotations.SerializedName
import com.kyu.tabungan.data.entity.BudgetEntity
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionEntity
import com.kyu.tabungan.data.entity.WalletEntity

data class BackupData(
    @SerializedName("version")
    val version: Int = 1,
    @SerializedName("appName")
    val appName: String = "Tabungan",
    @SerializedName("exportedAt")
    val exportedAt: Long = System.currentTimeMillis(),
    @SerializedName("wallets")
    val wallets: List<WalletEntity> = emptyList(),
    @SerializedName("categories")
    val categories: List<CategoryEntity> = emptyList(),
    @SerializedName("transactions")
    val transactions: List<TransactionEntity> = emptyList(),
    @SerializedName("budgets")
    val budgets: List<BudgetEntity> = emptyList(),
    @SerializedName("settings")
    val settings: Map<String, String> = emptyMap()
)

data class BackupSummary(
    val walletCount: Int,
    val categoryCount: Int,
    val transactionCount: Int,
    val budgetCount: Int,
    val exportedAt: Long
)
