package com.kyu.tabungan.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val targetAmount: Long,
    val savedAmount: Long = 0L,
    val dailyTarget: Long = 0L,
    val targetDate: Long? = null,
    val imageUri: String? = null,
    val icon: String = "phone",
    val colorHex: String = "#1687FF",
    val note: String = "",
    val isAchieved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
