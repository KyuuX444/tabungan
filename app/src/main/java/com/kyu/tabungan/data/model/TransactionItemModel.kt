package com.kyu.tabungan.data.model

import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.entity.WalletType

data class TransactionItemModel(
    val id: Long,
    val walletId: Long,
    val walletName: String,
    val walletType: WalletType,
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val type: TransactionType,
    val amount: Long,
    val note: String?,
    val date: Long,
    val createdAt: Long,
    val updatedAt: Long
)
