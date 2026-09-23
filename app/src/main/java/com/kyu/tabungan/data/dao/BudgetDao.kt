package com.kyu.tabungan.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kyu.tabungan.data.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

data class BudgetUsageTuple(
    val id: Long,
    val categoryId: Long?,
    val categoryName: String,
    val categoryIcon: String,
    val month: Int,
    val year: Int,
    val limitAmount: Long,
    val spentAmount: Long
)

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(budgets: List<BudgetEntity>): List<Long>

    @Update
    suspend fun update(budget: BudgetEntity)

    @Delete
    suspend fun delete(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT 
            b.id,
            b.categoryId,
            c.name AS categoryName,
            c.icon AS categoryIcon,
            b.month,
            b.year,
            b.limitAmount,
            COALESCE((
                SELECT SUM(t.amount) 
                FROM transactions t 
                WHERE t.categoryId = b.categoryId 
                  AND t.type = 'EXPENSE' 
                  AND t.date BETWEEN :startDate AND :endDate
            ), 0) AS spentAmount
        FROM budgets b
        INNER JOIN categories c ON b.categoryId = c.id
        WHERE b.month = :month AND b.year = :year
        ORDER BY b.createdAt ASC
    """)
    fun getBudgetsWithUsage(
        month: Int,
        year: Int,
        startDate: Long,
        endDate: Long
    ): Flow<List<BudgetUsageTuple>>

    @Query("SELECT COALESCE(SUM(limitAmount), 0) FROM budgets WHERE month = :month AND year = :year")
    fun getTotalBudgetLimit(month: Int, year: Int): Flow<Long>

    @Query("SELECT * FROM budgets WHERE id = :id LIMIT 1")
    suspend fun getBudgetById(id: Long): BudgetEntity?

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId AND month = :month AND year = :year LIMIT 1")
    suspend fun getBudgetByCategoryAndMonth(categoryId: Long, month: Int, year: Int): BudgetEntity?

    @Query("SELECT * FROM budgets")
    suspend fun getAllBudgetsSync(): List<BudgetEntity>

    @Query("DELETE FROM budgets")
    suspend fun deleteAll()
}
