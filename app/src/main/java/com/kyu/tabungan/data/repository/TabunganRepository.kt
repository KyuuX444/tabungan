package com.kyu.tabungan.data.repository

import com.kyu.tabungan.data.backup.BackupData
import com.kyu.tabungan.data.entity.BudgetEntity
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.entity.WalletEntity
import com.kyu.tabungan.data.model.BudgetWithUsage
import com.kyu.tabungan.data.model.CategorySpending
import com.kyu.tabungan.data.model.FinancialSummary
import com.kyu.tabungan.data.model.TransactionItemModel
import com.kyu.tabungan.data.model.WalletWithBalance
import kotlinx.coroutines.flow.Flow

interface TabunganRepository {
    fun getWalletsWithBalance(): Flow<List<WalletWithBalance>>
    fun getWalletWithBalanceById(id: Long): Flow<WalletWithBalance?>
    suspend fun insertWallet(wallet: WalletEntity): Long
    suspend fun updateWallet(wallet: WalletEntity)
    suspend fun deleteWallet(walletId: Long): Result<Unit>
    fun getTotalBalance(): Flow<Long>

    fun getAllCategories(): Flow<List<CategoryEntity>>
    fun getCategoriesByType(type: TransactionType): Flow<List<CategoryEntity>>
    fun getCategoryById(id: Long): Flow<CategoryEntity?>
    suspend fun insertCategory(category: CategoryEntity): Long
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(categoryId: Long): Result<Unit>

    fun getAllTransactions(): Flow<List<TransactionItemModel>>
    fun getRecentTransactions(limit: Int = 10): Flow<List<TransactionItemModel>>
    fun getTransactionById(id: Long): Flow<TransactionItemModel?>
    suspend fun getTransactionEntityById(id: Long): TransactionEntity?
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(id: Long)
    fun searchTransactions(
        query: String?,
        type: TransactionType?,
        walletId: Long?,
        categoryId: Long?,
        startDate: Long?,
        endDate: Long?
    ): Flow<List<TransactionItemModel>>

    fun getFinancialSummary(startDate: Long, endDate: Long): Flow<FinancialSummary>
    fun getCategorySpendings(startDate: Long, endDate: Long): Flow<List<CategorySpending>>

    fun getBudgetsWithUsage(month: Int, year: Int, startDate: Long, endDate: Long): Flow<List<BudgetWithUsage>>
    fun getTotalBudgetLimit(month: Int, year: Int): Flow<Long>
    suspend fun insertBudget(budget: BudgetEntity): Long
    suspend fun updateBudget(budget: BudgetEntity)
    suspend fun deleteBudget(id: Long)

    suspend fun loadSampleData()
    suspend fun clearAllData()
    suspend fun exportBackup(): BackupData
    suspend fun importBackup(backupData: BackupData): Result<Unit>
}
