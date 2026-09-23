package com.kyu.tabungan.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.NeoEmptyState
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.NeoTransactionItem
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.StatusSuccess
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted

@Composable
fun SearchTransactionScreen(
    viewModel: SearchTransactionViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
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
                text = "Cari Transaksi",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextMain
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val searchShape = RoundedCornerShape(12.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp, bottom = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 4.dp, y = 4.dp)
                    .background(HardShadowColor, shape = searchShape)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(searchShape)
                    .background(Surface)
                    .border(width = 2.dp, color = BorderColor, shape = searchShape)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = NeoIcons.Search,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (uiState.query.isEmpty()) {
                        Text(
                            text = "Cari catatan, kategori, nominal...",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }
                    BasicTextField(
                        value = uiState.query,
                        onValueChange = { viewModel.setQuery(it) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMain
                        ),
                        cursorBrush = SolidColor(BorderColor),
                        singleLine = true
                    )
                }
                if (uiState.query.isNotEmpty()) {
                    Icon(
                        imageVector = NeoIcons.Close,
                        contentDescription = "Hapus teks",
                        tint = TextMuted,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { viewModel.setQuery("") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    text = "Pemasukan",
                    isSelected = uiState.typeFilter == TransactionType.INCOME,
                    onClick = { viewModel.setTypeFilter(TransactionType.INCOME) },
                    activeColor = StatusSuccess
                )
            }
            item {
                FilterChip(
                    text = "Pengeluaran",
                    isSelected = uiState.typeFilter == TransactionType.EXPENSE,
                    onClick = { viewModel.setTypeFilter(TransactionType.EXPENSE) },
                    activeColor = StatusDanger
                )
            }
            item {
                FilterChip(
                    text = "Hari ini",
                    isSelected = uiState.dateFilter == DateFilterType.TODAY,
                    onClick = { viewModel.setDateFilter(DateFilterType.TODAY) }
                )
            }
            item {
                FilterChip(
                    text = "Minggu ini",
                    isSelected = uiState.dateFilter == DateFilterType.THIS_WEEK,
                    onClick = { viewModel.setDateFilter(DateFilterType.THIS_WEEK) }
                )
            }
            item {
                FilterChip(
                    text = "Bulan ini",
                    isSelected = uiState.dateFilter == DateFilterType.THIS_MONTH,
                    onClick = { viewModel.setDateFilter(DateFilterType.THIS_MONTH) }
                )
            }
            item {
                FilterChip(
                    text = "Bulan lalu",
                    isSelected = uiState.dateFilter == DateFilterType.LAST_MONTH,
                    onClick = { viewModel.setDateFilter(DateFilterType.LAST_MONTH) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searchResults.isEmpty()) {
            NeoEmptyState(
                title = "Tidak Ada Hasil",
                message = "Tidak ditemukan transaksi yang sesuai dengan kata kunci atau filter pencarian.",
                actionText = null,
                onActionClick = null
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(searchResults, key = { it.id }) { item ->
                    NeoTransactionItem(
                        item = item,
                        onClick = { onNavigateToDetail(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: androidx.compose.ui.graphics.Color = BrightBlue
) {
    val chipShape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier.padding(end = 2.dp, bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 2.dp, y = 2.dp)
                .background(HardShadowColor, shape = chipShape)
        )
        Box(
            modifier = Modifier
                .clip(chipShape)
                .background(if (isSelected) activeColor else Surface)
                .border(width = 1.5.dp, color = BorderColor, shape = chipShape)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Surface else TextMain
            )
        }
    }
}
