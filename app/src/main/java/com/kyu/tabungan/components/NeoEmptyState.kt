package com.kyu.tabungan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted

@Composable
fun NeoEmptyState(
    title: String = "Belum Ada Transaksi",
    message: String = "Mulai catat pemasukan dan pengeluaran\nuntuk mengetahui kondisi keuanganmu.",
    actionText: String? = "Tambah Transaksi",
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = NeoIcons.EmptyIllustration,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = androidx.compose.ui.graphics.Color.Unspecified
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextMain,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(20.dp))
            NeoButton(
                text = actionText,
                onClick = onActionClick,
                backgroundColor = BrightBlue,
                icon = NeoIcons.Plus
            )
        }
    }
}
