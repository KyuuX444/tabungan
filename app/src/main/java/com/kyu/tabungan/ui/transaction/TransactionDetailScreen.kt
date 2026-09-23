package com.kyu.tabungan.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoDialog
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.StatusSuccess
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter
import com.kyu.tabungan.util.DateUtils

@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val transaction by viewModel.transaction.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is DetailEvent.DeletedSuccess -> onNavigateBack()
            }
        }
    }

    if (showDeleteDialog) {
        NeoDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = "Hapus Transaksi",
            confirmText = "Hapus",
            confirmButtonColor = StatusDanger,
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteTransaction()
            }
        ) {
            Text(
                text = "Apakah Anda yakin ingin menghapus transaksi ini? Tindakan ini tidak dapat dibatalkan.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextMain
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeoIconButton(
                icon = NeoIcons.ChevronLeft,
                onClick = onNavigateBack,
                contentDescription = "Kembali",
                size = 42.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Detail Transaksi",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextMain
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (transaction == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Memuat detail transaksi...",
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }
        } else {
            val item = transaction!!
            val isIncome = item.type == TransactionType.INCOME
            val typeTitle = if (isIncome) "Pemasukan" else "Pengeluaran"
            val typeColor = if (isIncome) StatusSuccess else StatusDanger
            val catIcon = NeoIcons.getCategoryIcon(item.categoryIcon.ifEmpty { item.categoryName })

            NeoCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Surface,
                shadowOffset = 6.dp,
                cornerRadius = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isIncome) StatusSuccess.copy(alpha = 0.15f) else StatusDanger.copy(alpha = 0.15f))
                            .border(
                                width = 1.5.dp,
                                color = typeColor,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = typeTitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = typeColor
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = CurrencyFormatter.formatRupiahSigned(item.amount, isIncome),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = typeColor
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(BorderColor)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LightBlue)
                                .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = catIcon,
                                contentDescription = item.categoryName,
                                tint = TextMain,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Kategori",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = item.categoryName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                        }
                    }

                    if (!item.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "Catatan",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted
                                )
                                Text(
                                    text = item.note,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = TextMain
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tanggal & Waktu",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = DateUtils.formatDateTime(item.date),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Dompet",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = item.walletName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NeoButton(
                    text = "Edit",
                    onClick = { onNavigateToEdit(item.id) },
                    modifier = Modifier.weight(1f),
                    backgroundColor = BrightBlue,
                    contentColor = Surface,
                    icon = NeoIcons.Edit
                )

                NeoButton(
                    text = "Hapus",
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f),
                    backgroundColor = StatusDanger,
                    contentColor = Surface,
                    icon = NeoIcons.Trash
                )
            }
        }
    }
}
