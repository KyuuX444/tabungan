package com.kyu.tabungan.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kyu.tabungan.data.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

data class WalletBalanceTuple(
    val id: Long,
    val name: String,
    val type: com.kyu.tabungan.data.entity.WalletType,
    val initialBalance: Long,
    val icon: String,
    val createdAt: Long,
    val updatedAt: Long,
    val currentBalance: Long
)

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: WalletEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wallets: List<WalletEntity>): List<Long>

    @Update
    suspend fun update(wallet: WalletEntity)

    @Delete
    suspend fun delete(wallet: WalletEntity)

    @Query("DELETE FROM wallets WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM wallets ORDER BY createdAt ASC")
    fun getAllWallets(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets ORDER BY createdAt ASC")
    suspend fun getAllWalletsSync(): List<WalletEntity>

    @Query("SELECT * FROM wallets WHERE id = :id LIMIT 1")
    fun getWalletById(id: Long): Flow<WalletEntity?>

    @Query("SELECT * FROM wallets WHERE id = :id LIMIT 1")
    suspend fun getWalletByIdSync(id: Long): WalletEntity?

    @Query("""
        SELECT 
            w.id,
            w.name,
            w.type,
            w.initialBalance,
            w.icon,
            w.createdAt,
            w.updatedAt,
            (w.initialBalance + 
                COALESCE((SELECT SUM(amount) FROM transactions WHERE walletId = w.id AND type = 'INCOME'), 0) - 
                COALESCE((SELECT SUM(amount) FROM transactions WHERE walletId = w.id AND type = 'EXPENSE'), 0)
            ) AS currentBalance
        FROM wallets w
        ORDER BY w.createdAt ASC
    """)
    fun getWalletsWithBalance(): Flow<List<WalletBalanceTuple>>

    @Query("""
        SELECT 
            w.id,
            w.name,
            w.type,
            w.initialBalance,
            w.icon,
            w.createdAt,
            w.updatedAt,
            (w.initialBalance + 
                COALESCE((SELECT SUM(amount) FROM transactions WHERE walletId = w.id AND type = 'INCOME'), 0) - 
                COALESCE((SELECT SUM(amount) FROM transactions WHERE walletId = w.id AND type = 'EXPENSE'), 0)
            ) AS currentBalance
        FROM wallets w
        WHERE w.id = :id
        LIMIT 1
    """)
    fun getWalletWithBalanceById(id: Long): Flow<WalletBalanceTuple?>

    @Query("""
        SELECT 
            COALESCE((SELECT SUM(initialBalance) FROM wallets), 0) + 
            COALESCE((SELECT SUM(amount) FROM transactions WHERE type = 'INCOME'), 0) - 
            COALESCE((SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE'), 0)
    """)
    fun getTotalBalance(): Flow<Long?>

    @Query("SELECT COUNT(*) FROM transactions WHERE walletId = :walletId")
    suspend fun getTransactionCountForWallet(walletId: Long): Int

    @Query("SELECT COUNT(*) FROM wallets")
    suspend fun getWalletCount(): Int

    @Query("DELETE FROM wallets")
    suspend fun deleteAll()
}
