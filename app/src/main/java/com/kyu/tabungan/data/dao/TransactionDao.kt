package com.kyu.tabungan.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kyu.tabungan.data.entity.TransactionEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.model.TransactionItemModel
import kotlinx.coroutines.flow.Flow

data class CategorySpendingTuple(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val totalAmount: Long
)

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>): List<Long>

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT 
            t.id,
            t.walletId,
            w.name AS walletName,
            w.type AS walletType,
            t.categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            t.type,
            t.amount,
            t.note,
            t.date,
            t.createdAt,
            t.updatedAt
        FROM transactions t
        INNER JOIN wallets w ON t.walletId = w.id
        INNER JOIN categories c ON t.categoryId = c.id
        ORDER BY t.date DESC, t.id DESC
    """)
    fun getAllTransactions(): Flow<List<TransactionItemModel>>

    @Query("""
        SELECT 
            t.id,
            t.walletId,
            w.name AS walletName,
            w.type AS walletType,
            t.categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            t.type,
            t.amount,
            t.note,
            t.date,
            t.createdAt,
            t.updatedAt
        FROM transactions t
        INNER JOIN wallets w ON t.walletId = w.id
        INNER JOIN categories c ON t.categoryId = c.id
        ORDER BY t.date DESC, t.id DESC
        LIMIT :limit
    """)
    fun getRecentTransactions(limit: Int): Flow<List<TransactionItemModel>>

    @Query("""
        SELECT 
            t.id,
            t.walletId,
            w.name AS walletName,
            w.type AS walletType,
            t.categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            t.type,
            t.amount,
            t.note,
            t.date,
            t.createdAt,
            t.updatedAt
        FROM transactions t
        INNER JOIN wallets w ON t.walletId = w.id
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE t.id = :id
        LIMIT 1
    """)
    fun getTransactionById(id: Long): Flow<TransactionItemModel?>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionEntityById(id: Long): TransactionEntity?

    @Query("""
        SELECT 
            t.id,
            t.walletId,
            w.name AS walletName,
            w.type AS walletType,
            t.categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            t.type,
            t.amount,
            t.note,
            t.date,
            t.createdAt,
            t.updatedAt
        FROM transactions t
        INNER JOIN wallets w ON t.walletId = w.id
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE t.date BETWEEN :startDate AND :endDate
        ORDER BY t.date DESC, t.id DESC
    """)
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionItemModel>>

    @Query("""
        SELECT 
            t.id,
            t.walletId,
            w.name AS walletName,
            w.type AS walletType,
            t.categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            t.type,
            t.amount,
            t.note,
            t.date,
            t.createdAt,
            t.updatedAt
        FROM transactions t
        INNER JOIN wallets w ON t.walletId = w.id
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE (:query IS NULL OR :query = '' OR t.note LIKE '%' || :query || '%' OR c.name LIKE '%' || :query || '%' OR w.name LIKE '%' || :query || '%' OR CAST(t.amount AS TEXT) LIKE '%' || :query || '%')
          AND (:type IS NULL OR t.type = :type)
          AND (:walletId IS NULL OR t.walletId = :walletId)
          AND (:categoryId IS NULL OR t.categoryId = :categoryId)
          AND (:startDate IS NULL OR t.date >= :startDate)
          AND (:endDate IS NULL OR t.date <= :endDate)
        ORDER BY t.date DESC, t.id DESC
    """)
    fun searchTransactions(
        query: String?,
        type: TransactionType?,
        walletId: Long?,
        categoryId: Long?,
        startDate: Long?,
        endDate: Long?
    ): Flow<List<TransactionItemModel>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME' AND date BETWEEN :startDate AND :endDate")
    fun getIncomeSumByDateRange(startDate: Long, endDate: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN :startDate AND :endDate")
    fun getExpenseSumByDateRange(startDate: Long, endDate: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME'")
    fun getTotalIncome(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE'")
    fun getTotalExpense(): Flow<Long>

    @Query("""
        SELECT 
            c.id AS categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            COALESCE(SUM(t.amount), 0) AS totalAmount
        FROM transactions t
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE t.type = 'EXPENSE' AND t.date BETWEEN :startDate AND :endDate
        GROUP BY c.id, c.name, c.icon
        ORDER BY totalAmount DESC
    """)
    fun getCategorySpendingByDateRange(startDate: Long, endDate: Long): Flow<List<CategorySpendingTuple>>

    @Query("SELECT * FROM transactions ORDER BY date ASC")
    suspend fun getAllTransactionsSync(): List<TransactionEntity>

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}
