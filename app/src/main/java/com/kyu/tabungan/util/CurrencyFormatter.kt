package com.kyu.tabungan.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyFormatter {
    private val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val formatter = DecimalFormat("#,###", symbols)

    fun formatRupiah(amount: Long, withPrefix: Boolean = true): String {
        val formatted = formatter.format(kotlin.math.abs(amount))
        val prefix = if (withPrefix) "Rp" else ""
        return if (amount < 0) {
            "-$prefix$formatted"
        } else {
            "$prefix$formatted"
        }
    }

    fun formatRupiahSigned(amount: Long, isIncome: Boolean): String {
        val formatted = formatter.format(kotlin.math.abs(amount))
        return if (isIncome) {
            "+Rp$formatted"
        } else {
            "-Rp$formatted"
        }
    }

    fun parseAmount(input: String): Long {
        val clean = input.filter { it.isDigit() }
        return clean.toLongOrNull() ?: 0L
    }
}
