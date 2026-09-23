package com.kyu.tabungan.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kyu.tabungan.data.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsGoalDao {
    @Query("SELECT * FROM savings_goals ORDER BY isAchieved ASC, createdAt DESC")
    fun getAllGoals(): Flow<List<SavingsGoalEntity>>

    @Query("SELECT * FROM savings_goals WHERE id = :id")
    fun getGoalById(id: Long): Flow<SavingsGoalEntity?>

    @Query("SELECT * FROM savings_goals WHERE id = :id")
    suspend fun getGoalByIdSync(id: Long): SavingsGoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: SavingsGoalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(goals: List<SavingsGoalEntity>)

    @Update
    suspend fun update(goal: SavingsGoalEntity)

    @Query("UPDATE savings_goals SET savedAmount = :newAmount, isAchieved = :isAchieved WHERE id = :id")
    suspend fun updateSavedAmount(id: Long, newAmount: Long, isAchieved: Boolean)

    @Query("DELETE FROM savings_goals WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM savings_goals")
    suspend fun deleteAll()
}
