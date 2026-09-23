package com.kyu.tabungan.data.model

data class BudgetWithUsage(
    val id: Long,
    val categoryId: Long?,
    val categoryName: String,
    val categoryIcon: String,
    val month: Int,
    val year: Int,
    val limitAmount: Long,
    val spentAmount: Long
) {
    val remainingAmount: Long get() = limitAmount - spentAmount
    val isOverBudget: Boolean get() = spentAmount > limitAmount
    val progress: Float
        get() {
            if (limitAmount <= 0L) return 0f
            return (spentAmount.toFloat() / limitAmount.toFloat()).coerceAtLeast(0f)
        }
    val percentageInt: Int
        get() {
            if (limitAmount <= 0L) return 0
            return ((spentAmount.toDouble() / limitAmount.toDouble()) * 100).toInt()
        }
}
