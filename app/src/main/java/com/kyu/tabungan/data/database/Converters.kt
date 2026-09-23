package com.kyu.tabungan.data.database

import androidx.room.TypeConverter
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.entity.WalletType

class Converters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType?): String? {
        return type?.name
    }

    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? {
        return value?.let { TransactionType.valueOf(it) }
    }

    @TypeConverter
    fun fromWalletType(type: WalletType?): String? {
        return type?.name
    }

    @TypeConverter
    fun toWalletType(value: String?): WalletType? {
        return value?.let { WalletType.valueOf(it) }
    }
}
