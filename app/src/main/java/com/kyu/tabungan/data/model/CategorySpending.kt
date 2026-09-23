package com.kyu.tabungan.data.model

data class CategorySpending(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val totalAmount: Long,
    val percentage: Float
)
