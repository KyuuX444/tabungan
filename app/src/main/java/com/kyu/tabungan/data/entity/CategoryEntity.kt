package com.kyu.tabungan.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: TransactionType,
    val icon: String,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
