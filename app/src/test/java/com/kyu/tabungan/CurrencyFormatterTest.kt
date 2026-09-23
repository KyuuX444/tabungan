package com.kyu.tabungan

import com.kyu.tabungan.util.CurrencyFormatter
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyFormatterTest {

    @Test
    fun testFormatRupiah() {
        assertEquals("Rp25.000", CurrencyFormatter.formatRupiah(25000L))
        assertEquals("Rp350.000", CurrencyFormatter.formatRupiah(350000L))
        assertEquals("Rp1.500.000", CurrencyFormatter.formatRupiah(1500000L))
        assertEquals("Rp10.000.000", CurrencyFormatter.formatRupiah(10000000L))
        assertEquals("Rp0", CurrencyFormatter.formatRupiah(0L))
    }

    @Test
    fun testFormatRupiahSigned() {
        assertEquals("+Rp4.000.000", CurrencyFormatter.formatRupiahSigned(4000000L, isIncome = true))
        assertEquals("-Rp1.550.000", CurrencyFormatter.formatRupiahSigned(1550000L, isIncome = false))
    }

    @Test
    fun testParseAmount() {
        assertEquals(25000L, CurrencyFormatter.parseAmount("25.000"))
        assertEquals(1500000L, CurrencyFormatter.parseAmount("1.500.000"))
        assertEquals(0L, CurrencyFormatter.parseAmount(""))
        assertEquals(500L, CurrencyFormatter.parseAmount("Rp 500"))
    }
}
