package com.kyu.tabungan.ui.statistics

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoEmptyState
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.NeoProgressBar
import com.kyu.tabungan.components.NeoStatCard
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.DeepBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.SkyBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.StatusSuccess
import com.kyu.tabungan.theme.StatusWarning
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val summary by viewModel.financialSummary.collectAsState()
    val categorySpendings by viewModel.categorySpendings.collectAsState()

    val chartColors = listOf(
        BrightBlue,
        DeepBlue,
        SkyBlue,
        StatusSuccess,
        StatusWarning,
        Color(0xFFFF8552),
        Color(0xFF8338EC),
        TextMuted
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Analisis Keuangan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
                Text(
                    text = "Statistik",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodTab(
                    text = "Minggu",
                    isSelected = uiState.period == StatisticsPeriod.WEEK,
                    onClick = { viewModel.setPeriod(StatisticsPeriod.WEEK) },
                    modifier = Modifier.weight(1f)
                )
                PeriodTab(
                    text = "Bulan",
                    isSelected = uiState.period == StatisticsPeriod.MONTH,
                    onClick = { viewModel.setPeriod(StatisticsPeriod.MONTH) },
                    modifier = Modifier.weight(1f)
                )
                PeriodTab(
                    text = "Tahun",
                    isSelected = uiState.period == StatisticsPeriod.YEAR,
                    onClick = { viewModel.setPeriod(StatisticsPeriod.YEAR) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            NeoCard(
                modifier = Modifier.fillMaxWidth(),
                shadowOffset = 4.dp,
                cornerRadius = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NeoIconButton(
                        icon = NeoIcons.ChevronLeft,
                        onClick = { viewModel.previousPeriod() },
                        contentDescription = "Periode Sebelumnya",
                        size = 36.dp,
                        shadowOffset = 2.dp,
                        borderWidth = 1.5.dp
                    )

                    Text(
                        text = uiState.periodLabel,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )

                    NeoIconButton(
                        icon = NeoIcons.ChevronRight,
                        onClick = { viewModel.nextPeriod() },
                        contentDescription = "Periode Selanjutnya",
                        size = 36.dp,
                        shadowOffset = 2.dp,
                        borderWidth = 1.5.dp
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeoStatCard(
                    title = "Pemasukan",
                    amount = summary.totalIncome,
                    icon = NeoIcons.ArrowDown,
                    iconBgColor = Color(0xFFE8F9EE),
                    amountColor = StatusSuccess,
                    modifier = Modifier.weight(1f)
                )

                NeoStatCard(
                    title = "Pengeluaran",
                    amount = summary.totalExpense,
                    icon = NeoIcons.ArrowUp,
                    iconBgColor = Color(0xFFFFECEC),
                    amountColor = StatusDanger,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            NeoCard(
                modifier = Modifier.fillMaxWidth(),
                shadowOffset = 5.dp,
                cornerRadius = 14.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Selisih Periode Ini",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CurrencyFormatter.formatRupiah(summary.netBalance),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = if (summary.netBalance >= 0) StatusSuccess else StatusDanger
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (summary.netBalance >= 0) LightBlue else Color(0xFFFFE5E5))
                            .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (summary.netBalance >= 0) "Surplus" else "Defisit",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (summary.netBalance >= 0) BrightBlue else StatusDanger
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Pengeluaran Berdasarkan Kategori",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = TextMain,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (categorySpendings.isEmpty()) {
            item {
                NeoEmptyState(
                    title = "Belum Ada Pengeluaran",
                    message = "Tidak ada transaksi pengeluaran pada periode ini.",
                    actionText = null,
                    onActionClick = null
                )
            }
        } else {
            item {
                NeoCard(
                    modifier = Modifier.fillMaxWidth(),
                    shadowOffset = 5.dp,
                    cornerRadius = 14.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(170.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                                var startAngle = -90f
                                categorySpendings.forEachIndexed { index, item ->
                                    val sweepAngle = (item.percentage / 100f) * 360f
                                    val color = chartColors[index % chartColors.size]
                                    drawArc(
                                        color = color,
                                        startAngle = startAngle,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(width = 28f, cap = StrokeCap.Butt)
                                    )
                                    startAngle += sweepAngle
                                }
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted
                                )
                                Text(
                                    text = CurrencyFormatter.formatRupiah(summary.totalExpense),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextMain
                                )
                            }
                        }
                    }
                }
            }

            items(categorySpendings) { item ->
                val index = categorySpendings.indexOf(item)
                val color = chartColors[index % chartColors.size]
                val catIcon = NeoIcons.getCategoryIcon(item.categoryIcon.ifEmpty { item.categoryName })

                NeoCard(
                    modifier = Modifier.fillMaxWidth(),
                    shadowOffset = 4.dp,
                    borderWidth = 2.dp,
                    cornerRadius = 12.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color.copy(alpha = 0.2f))
                                        .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = catIcon,
                                        contentDescription = item.categoryName,
                                        tint = TextMain,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = item.categoryName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = CurrencyFormatter.formatRupiah(item.totalAmount),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextMain
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${item.percentage.toInt()}%",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        NeoProgressBar(
                            progress = item.percentage / 100f,
                            barColor = color,
                            height = 8.dp,
                            cornerRadius = 4.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PeriodTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier.padding(end = 2.dp, bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = 2.dp, y = 2.dp)
                .background(HardShadowColor, shape = shape)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(if (isSelected) BrightBlue else Surface)
                .border(width = 2.dp, color = BorderColor, shape = shape)
                .clickable(onClick = onClick)
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Surface else TextMain
            )
        }
    }
}
