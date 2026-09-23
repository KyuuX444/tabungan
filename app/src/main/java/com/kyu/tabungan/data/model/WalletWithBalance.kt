package com.kyu.tabungan.data.model

import com.kyu.tabungan.data.entity.WalletEntity
import com.kyu.tabungan.data.entity.WalletType

data class WalletWithBalance(
    val wallet: WalletEntity,
    val currentBalance: Long
) {
    val id: Long get() = wallet.id
    val name: String get() = wallet.name
    val type: WalletType get() = wallet.type
    val icon: String get() = wallet.icon
    val initialBalance: Long get() = wallet.initialBalance
}
