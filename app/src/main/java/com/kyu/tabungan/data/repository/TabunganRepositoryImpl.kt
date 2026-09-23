package com.kyu.tabungan.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.kyu.tabungan.data.backup.BackupData
import com.kyu.tabungan.data.database.TabunganDatabase
import com.kyu.tabungan.data.entity.BudgetEntity
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.entity.WalletEntity
import com.kyu.tabungan.data.entity.WalletType
import com.kyu.tabungan.data.model.BudgetWithUsage
import com.kyu.tabungan.data.model.CategorySpending
import com.kyu.tabungan.data.model.FinancialSummary
import com.kyu.tabungan.data.model.TransactionItemModel
import com.kyu.tabungan.data.model.WalletWithBalance
import com.kyu.tabungan.widget.TabunganWidgetUpdater
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Calendar

class TabunganRepositoryImpl(
    private val context: Context,
    private val database: TabunganDatabase
) : TabunganRepository {

    private val walletDao = database.walletDao()
    private val categoryDao = database.categoryDao()
    private val transactionDao = database.transactionDao()
    private val budgetDao = database.budgetDao()
    private val savingsGoalDao = database.savingsGoalDao()

    override fun getWalletsWithBalance(): Flow<List<WalletWithBalance>> {
        return walletDao.getWalletsWithBalance().map { list ->
            list.map { tuple ->
                WalletWithBalance(
                    wallet = WalletEntity(
                        id = tuple.id,
                        name = tuple.name,
                        type = tuple.type,
                        initialBalance = tuple.initialBalance,
                        icon = tuple.icon,
                        createdAt = tuple.createdAt,
                        updatedAt = tuple.updatedAt
                    ),
                    currentBalance = tuple.currentBalance
                )
            }
        }
    }

    override fun getWalletWithBalanceById(id: Long): Flow<WalletWithBalance?> {
        return walletDao.getWalletWithBalanceById(id).map { tuple ->
            tuple?.let {
                WalletWithBalance(
                    wallet = WalletEntity(
                        id = it.id,
                        name = it.name,
                        type = it.type,
                        initialBalance = it.initialBalance,
                        icon = it.icon,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt
                    ),
                    currentBalance = it.currentBalance
                )
            }
        }
    }

    override suspend fun insertWallet(wallet: WalletEntity): Long {
        val id = walletDao.insert(wallet)
        TabunganWidgetUpdater.updateAll(context)
        return id
    }

    override suspend fun updateWallet(wallet: WalletEntity) {
        walletDao.update(wallet.copy(updatedAt = System.currentTimeMillis()))
        TabunganWidgetUpdater.updateAll(context)
    }

    override suspend fun deleteWallet(walletId: Long): Result<Unit> {
        val count = walletDao.getTransactionCountForWallet(walletId)
        if (count > 0) {
            return Result.failure(IllegalStateException("Dompet tidak dapat dihapus karena masih digunakan oleh $count transaksi."))
        }
        val totalWallets = walletDao.getWalletCount()
        if (totalWallets <= 1) {
            return Result.failure(IllegalStateException("Aplikasi membutuhkan setidaknya satu dompet aktif."))
        }
        walletDao.deleteById(walletId)
        TabunganWidgetUpdater.updateAll(context)
        return Result.success(Unit)
    }

    override fun getTotalBalance(): Flow<Long> {
        return walletDao.getTotalBalance().map { it ?: 0L }
    }

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<CategoryEntity>> {
        return categoryDao.getCategoriesByType(type)
    }

    override fun getCategoryById(id: Long): Flow<CategoryEntity?> {
        return categoryDao.getCategoryById(id)
    }

    override suspend fun insertCategory(category: CategoryEntity): Long {
        return categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.update(category)
    }

    override suspend fun deleteCategory(categoryId: Long): Result<Unit> {
        val count = categoryDao.getTransactionCountForCategory(categoryId)
        if (count > 0) {
            return Result.failure(IllegalStateException("Kategori tidak dapat dihapus karena masih digunakan oleh $count transaksi."))
        }
        categoryDao.deleteById(categoryId)
        return Result.success(Unit)
    }

    override fun getAllTransactions(): Flow<List<TransactionItemModel>> {
        return transactionDao.getAllTransactions()
    }

    override fun getRecentTransactions(limit: Int): Flow<List<TransactionItemModel>> {
        return transactionDao.getRecentTransactions(limit)
    }

    override fun getTransactionById(id: Long): Flow<TransactionItemModel?> {
        return transactionDao.getTransactionById(id)
    }

    override suspend fun getTransactionEntityById(id: Long): TransactionEntity? {
        return transactionDao.getTransactionEntityById(id)
    }

    override suspend fun insertTransaction(transaction: TransactionEntity): Long {
        val id = transactionDao.insert(transaction)
        TabunganWidgetUpdater.updateAll(context)
        return id
    }

    override suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.update(transaction.copy(updatedAt = System.currentTimeMillis()))
        TabunganWidgetUpdater.updateAll(context)
    }

    override suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteById(id)
        TabunganWidgetUpdater.updateAll(context)
    }

    override fun searchTransactions(
        query: String?,
        type: TransactionType?,
        walletId: Long?,
        categoryId: Long?,
        startDate: Long?,
        endDate: Long?
    ): Flow<List<TransactionItemModel>> {
        return transactionDao.searchTransactions(query, type, walletId, categoryId, startDate, endDate)
    }

    override fun getFinancialSummary(startDate: Long, endDate: Long): Flow<FinancialSummary> {
        val incomeFlow = transactionDao.getIncomeSumByDateRange(startDate, endDate)
        val expenseFlow = transactionDao.getExpenseSumByDateRange(startDate, endDate)

        return combine(incomeFlow, expenseFlow) { income, expense ->
            FinancialSummary(
                totalIncome = income,
                totalExpense = expense,
                netBalance = income - expense
            )
        }
    }

    override fun getCategorySpendings(startDate: Long, endDate: Long): Flow<List<CategorySpending>> {
        return transactionDao.getCategorySpendingByDateRange(startDate, endDate).map { list ->
            val totalExpense = list.sumOf { it.totalAmount }
            list.map { item ->
                val percentage = if (totalExpense > 0L) {
                    ((item.totalAmount.toDouble() / totalExpense.toDouble()) * 100).toFloat()
                } else {
                    0f
                }
                CategorySpending(
                    categoryId = item.categoryId,
                    categoryName = item.categoryName,
                    categoryIcon = item.categoryIcon,
                    totalAmount = item.totalAmount,
                    percentage = percentage
                )
            }
        }
    }

    override fun getBudgetsWithUsage(
        month: Int,
        year: Int,
        startDate: Long,
        endDate: Long
    ): Flow<List<BudgetWithUsage>> {
        return budgetDao.getBudgetsWithUsage(month, year, startDate, endDate).map { list ->
            list.map { tuple ->
                BudgetWithUsage(
                    id = tuple.id,
                    categoryId = tuple.categoryId,
                    categoryName = tuple.categoryName,
                    categoryIcon = tuple.categoryIcon,
                    month = tuple.month,
                    year = tuple.year,
                    limitAmount = tuple.limitAmount,
                    spentAmount = tuple.spentAmount
                )
            }
        }
    }

    override fun getTotalBudgetLimit(month: Int, year: Int): Flow<Long> {
        return budgetDao.getTotalBudgetLimit(month, year)
    }

    override suspend fun insertBudget(budget: BudgetEntity): Long {
        return budgetDao.insert(budget)
    }

    override suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.update(budget.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteBudget(id: Long) {
        budgetDao.deleteById(id)
    }

    override fun getAllSavingsGoals(): Flow<List<com.kyu.tabungan.data.entity.SavingsGoalEntity>> {
        return savingsGoalDao.getAllGoals()
    }

    override fun getSavingsGoalById(id: Long): Flow<com.kyu.tabungan.data.entity.SavingsGoalEntity?> {
        return savingsGoalDao.getGoalById(id)
    }

    override suspend fun insertSavingsGoal(goal: com.kyu.tabungan.data.entity.SavingsGoalEntity): Long {
        return savingsGoalDao.insert(goal)
    }

    override suspend fun updateSavingsGoal(goal: com.kyu.tabungan.data.entity.SavingsGoalEntity) {
        savingsGoalDao.update(goal)
    }

    override suspend fun deleteSavingsGoal(id: Long) {
        savingsGoalDao.deleteById(id)
    }

    override suspend fun addSavedAmountToGoal(goalId: Long, amountToAdd: Long) {
        val currentGoal = savingsGoalDao.getGoalByIdSync(goalId) ?: return
        val newAmount = (currentGoal.savedAmount + amountToAdd).coerceAtLeast(0L)
        val isAchieved = newAmount >= currentGoal.targetAmount
        savingsGoalDao.updateSavedAmount(goalId, newAmount, isAchieved)
    }

    override suspend fun loadSampleData() {
        database.withTransaction {
            savingsGoalDao.deleteAll()
            budgetDao.deleteAll()
            transactionDao.deleteAll()
            categoryDao.deleteAll()
            walletDao.deleteAll()

            TabunganDatabase.populateDefaults(database)

            val wallets = listOf(
                WalletEntity(id = 1L, name = "Tunai", type = WalletType.CASH, initialBalance = 350000L, icon = "cash"),
                WalletEntity(id = 2L, name = "Bank", type = WalletType.BANK, initialBalance = 1500000L, icon = "bank"),
                WalletEntity(id = 3L, name = "E-Wallet", type = WalletType.EWALLET, initialBalance = 600000L, icon = "ewallet")
            )
            walletDao.insertAll(wallets)

            val categories = categoryDao.getAllCategoriesSync()
            val gajiCat = categories.find { it.name == "Gaji" }?.id ?: 1L
            val makanCat = categories.find { it.name == "Makanan" }?.id ?: 6L
            val transportCat = categories.find { it.name == "Transportasi" }?.id ?: 7L
            val belanjaCat = categories.find { it.name == "Belanja" }?.id ?: 8L
            val hiburanCat = categories.find { it.name == "Hiburan" }?.id ?: 9L

            val now = System.currentTimeMillis()
            val sampleTransactions = listOf(
                TransactionEntity(
                    walletId = 2L,
                    categoryId = gajiCat,
                    type = TransactionType.INCOME,
                    amount = 4000000L,
                    note = "Gaji bulanan",
                    date = now - 86400000L * 2
                ),
                TransactionEntity(
                    walletId = 1L,
                    categoryId = makanCat,
                    type = TransactionType.EXPENSE,
                    amount = 25000L,
                    note = "Makan siang",
                    date = now - 3600000L * 4
                ),
                TransactionEntity(
                    walletId = 1L,
                    categoryId = transportCat,
                    type = TransactionType.EXPENSE,
                    amount = 15000L,
                    note = "Perjalanan kerja",
                    date = now - 3600000L * 2
                ),
                TransactionEntity(
                    walletId = 3L,
                    categoryId = belanjaCat,
                    type = TransactionType.EXPENSE,
                    amount = 300000L,
                    note = "Belanja bulanan",
                    date = now - 86400000L
                ),
                TransactionEntity(
                    walletId = 3L,
                    categoryId = hiburanCat,
                    type = TransactionType.EXPENSE,
                    amount = 150000L,
                    note = "Nonton bioskop",
                    date = now - 3600000L * 8
                )
            )
            transactionDao.insertAll(sampleTransactions)

            val cal = Calendar.getInstance()
            val currentMonth = cal.get(Calendar.MONTH) + 1
            val currentYear = cal.get(Calendar.YEAR)

            val sampleBudgets = listOf(
                BudgetEntity(
                    categoryId = makanCat,
                    month = currentMonth,
                    year = currentYear,
                    limitAmount = 500000L
                ),
                BudgetEntity(
                    categoryId = transportCat,
                    month = currentMonth,
                    year = currentYear,
                    limitAmount = 400000L
                ),
                BudgetEntity(
                    categoryId = belanjaCat,
                    month = currentMonth,
                    year = currentYear,
                    limitAmount = 300000L
                )
            )
            budgetDao.insertAll(sampleBudgets)
        }
        TabunganWidgetUpdater.updateAll(context)
    }

    override suspend fun clearAllData() {
        database.withTransaction {
            savingsGoalDao.deleteAll()
            budgetDao.deleteAll()
            transactionDao.deleteAll()
            categoryDao.deleteAll()
            walletDao.deleteAll()
            TabunganDatabase.populateDefaults(database)
        }
        TabunganWidgetUpdater.updateAll(context)
    }

    override suspend fun exportBackup(): BackupData {
        val wallets = walletDao.getAllWalletsSync()
        val categories = categoryDao.getAllCategoriesSync()
        val transactions = transactionDao.getAllTransactionsSync()
        val budgets = budgetDao.getAllBudgetsSync()

        return BackupData(
            version = 1,
            appName = "Tabungan",
            exportedAt = System.currentTimeMillis(),
            wallets = wallets,
            categories = categories,
            transactions = transactions,
            budgets = budgets,
            settings = mapOf("currency" to "IDR")
        )
    }

    override suspend fun importBackup(backupData: BackupData): Result<Unit> {
        return try {
            database.withTransaction {
                budgetDao.deleteAll()
                transactionDao.deleteAll()
                categoryDao.deleteAll()
                walletDao.deleteAll()

                if (backupData.wallets.isNotEmpty()) {
                    walletDao.insertAll(backupData.wallets)
                } else {
                    TabunganDatabase.populateDefaults(database)
                }

                if (backupData.categories.isNotEmpty()) {
                    categoryDao.insertAll(backupData.categories)
                }

                if (backupData.transactions.isNotEmpty()) {
                    transactionDao.insertAll(backupData.transactions)
                }

                if (backupData.budgets.isNotEmpty()) {
                    budgetDao.insertAll(backupData.budgets)
                }
            }
            TabunganWidgetUpdater.updateAll(context)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
