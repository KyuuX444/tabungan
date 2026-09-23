package com.kyu.tabungan

import com.kyu.tabungan.data.backup.BackupData
import com.kyu.tabungan.data.backup.BackupManager
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.entity.WalletEntity
import com.kyu.tabungan.data.entity.WalletType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupManagerTest {

    private val backupManager = BackupManager()

    @Test
    fun testExportAndValidate() {
        val sampleData = BackupData(
            wallets = listOf(
                WalletEntity(id = 1L, name = "Tunai", type = WalletType.CASH, initialBalance = 100000L, icon = "cash")
            ),
            categories = listOf(
                CategoryEntity(id = 1L, name = "Makanan", type = TransactionType.EXPENSE, icon = "food")
            ),
            transactions = listOf(
                TransactionEntity(id = 1L, walletId = 1L, categoryId = 1L, type = TransactionType.EXPENSE, amount = 20000L, date = 1000L)
            )
        )

        val json = backupManager.exportToJson(sampleData)
        assertTrue(json.contains("Tabungan"))

        val result = backupManager.parseAndValidate(json)
        assertTrue(result.isSuccess)

        val (_, summary) = result.getOrThrow()
        assertEquals(1, summary.walletCount)
        assertEquals(1, summary.categoryCount)
        assertEquals(1, summary.transactionCount)
    }

    @Test
    fun testInvalidJsonFails() {
        val result = backupManager.parseAndValidate("invalid json")
        assertTrue(result.isFailure)
    }
}
