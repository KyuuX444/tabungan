package com.kyu.tabungan.data.backup

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException

class BackupManager {
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun exportToJson(data: BackupData): String {
        return gson.toJson(data)
    }

    fun parseAndValidate(jsonString: String): Result<Pair<BackupData, BackupSummary>> {
        return try {
            val trimmed = jsonString.trim()
            if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
                return Result.failure(IllegalArgumentException("Format berkas tidak valid, harus berupa objek JSON."))
            }

            val data = gson.fromJson(jsonString, BackupData::class.java)
                ?: return Result.failure(IllegalArgumentException("Data cadangan kosong atau tidak valid."))

            if (data.appName != "Tabungan") {
                return Result.failure(IllegalArgumentException("Berkas bukan berasal dari aplikasi Tabungan."))
            }

            for (w in data.wallets) {
                if (w.name.isBlank()) {
                    return Result.failure(IllegalArgumentException("Data dompet tidak valid: nama dompet tidak boleh kosong."))
                }
            }

            for (c in data.categories) {
                if (c.name.isBlank()) {
                    return Result.failure(IllegalArgumentException("Data kategori tidak valid: nama kategori tidak boleh kosong."))
                }
            }

            for (t in data.transactions) {
                if (t.amount <= 0) {
                    return Result.failure(IllegalArgumentException("Data transaksi tidak valid: nominal harus lebih besar dari 0."))
                }
            }

            val summary = BackupSummary(
                walletCount = data.wallets.size,
                categoryCount = data.categories.size,
                transactionCount = data.transactions.size,
                budgetCount = data.budgets.size,
                exportedAt = data.exportedAt
            )

            Result.success(Pair(data, summary))
        } catch (e: JsonSyntaxException) {
            Result.failure(IllegalArgumentException("Sintaks JSON rusak: ${e.localizedMessage ?: "Tidak diketahui"}"))
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Gagal membaca berkas cadangan: ${e.localizedMessage ?: "Tidak diketahui"}"))
        }
    }
}
