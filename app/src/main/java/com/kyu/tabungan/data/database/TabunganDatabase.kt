package com.kyu.tabungan.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kyu.tabungan.data.dao.BudgetDao
import com.kyu.tabungan.data.dao.CategoryDao
import com.kyu.tabungan.data.dao.TransactionDao
import com.kyu.tabungan.data.dao.WalletDao
import com.kyu.tabungan.data.entity.BudgetEntity
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.entity.WalletEntity
import com.kyu.tabungan.data.entity.WalletType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        WalletEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TabunganDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: TabunganDatabase? = null

        fun getInstance(context: Context): TabunganDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TabunganDatabase::class.java,
                    "tabungan.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                populateDefaults(getInstance(context))
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDefaults(database: TabunganDatabase) {
            val defaultWallets = listOf(
                WalletEntity(name = "Tunai", type = WalletType.CASH, initialBalance = 0L, icon = "cash"),
                WalletEntity(name = "Bank", type = WalletType.BANK, initialBalance = 0L, icon = "bank"),
                WalletEntity(name = "E-Wallet", type = WalletType.EWALLET, initialBalance = 0L, icon = "ewallet")
            )
            database.walletDao().insertAll(defaultWallets)

            val defaultIncomeCategories = listOf(
                CategoryEntity(name = "Gaji", type = TransactionType.INCOME, icon = "salary", isDefault = true),
                CategoryEntity(name = "Freelance", type = TransactionType.INCOME, icon = "freelance", isDefault = true),
                CategoryEntity(name = "Bisnis", type = TransactionType.INCOME, icon = "business", isDefault = true),
                CategoryEntity(name = "Hadiah", type = TransactionType.INCOME, icon = "gift", isDefault = true),
                CategoryEntity(name = "Lainnya", type = TransactionType.INCOME, icon = "other", isDefault = true)
            )

            val defaultExpenseCategories = listOf(
                CategoryEntity(name = "Makanan", type = TransactionType.EXPENSE, icon = "food", isDefault = true),
                CategoryEntity(name = "Transportasi", type = TransactionType.EXPENSE, icon = "transport", isDefault = true),
                CategoryEntity(name = "Belanja", type = TransactionType.EXPENSE, icon = "shopping", isDefault = true),
                CategoryEntity(name = "Hiburan", type = TransactionType.EXPENSE, icon = "entertainment", isDefault = true),
                CategoryEntity(name = "Tagihan", type = TransactionType.EXPENSE, icon = "bills", isDefault = true),
                CategoryEntity(name = "Pendidikan", type = TransactionType.EXPENSE, icon = "education", isDefault = true),
                CategoryEntity(name = "Kesehatan", type = TransactionType.EXPENSE, icon = "health", isDefault = true),
                CategoryEntity(name = "Lainnya", type = TransactionType.EXPENSE, icon = "other", isDefault = true)
            )

            database.categoryDao().insertAll(defaultIncomeCategories + defaultExpenseCategories)
        }
    }
}
