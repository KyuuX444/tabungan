package com.kyu.tabungan.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoEmptyState
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.NeoProgressBar
import com.kyu.tabungan.components.NeoTransactionItem
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.DeepBlue
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.StatusSuccess
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter
import com.kyu.tabungan.util.DateUtils

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactionDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToGoals: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val greeting = DateUtils.getGreeting()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = greeting,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Text(
                        text = "Tabungan Saya",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                }

                NeoIconButton(
                    icon = NeoIcons.Search,
                    onClick = onNavigateToSearch,
                    contentDescription = "Cari Transaksi",
                    size = 42.dp
                )
            }
        }

        item {
            NeoCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = BrightBlue,
                shadowOffset = 6.dp,
                cornerRadius = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "TOTAL SALDO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Surface.copy(alpha = 0.9f),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = CurrencyFormatter.formatRupiah(uiState.totalBalance),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = Surface
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Surface)
                                .border(width = 2.dp, color = BorderColor, shape = RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE8F9EE))
                                            .border(width = 1.dp, color = BorderColor, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = NeoIcons.ArrowDown,
                                            contentDescription = null,
                                            tint = StatusSuccess,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Pemasukan",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "+${CurrencyFormatter.formatRupiah(uiState.monthlyIncome)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = StatusSuccess
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Surface)
                                .border(width = 2.dp, color = BorderColor, shape = RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFECEC))
                                            .border(width = 1.dp, color = BorderColor, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = NeoIcons.ArrowUp,
                                            contentDescription = null,
                                            tint = StatusDanger,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Pengeluaran",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "-${CurrencyFormatter.formatRupiah(uiState.monthlyExpense)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = StatusDanger
                                )
                            }
                        }
                    }
                }
            }
        }

        if (uiState.monthlyBudgetLimit > 0L) {
            item {
                NeoCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Surface,
                    shadowOffset = 5.dp,
                    cornerRadius = 14.dp,
                    onClick = onNavigateToBudgets
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(LightBlue)
                                        .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = NeoIcons.Budget,
                                        contentDescription = null,
                                        tint = BrightBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Anggaran Bulanan",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMain
                                )
                            }

                            val isOver = uiState.monthlyBudgetSpent > uiState.monthlyBudgetLimit
                            Text(
                                text = if (isOver) "Melebihi" else "Sisa: ${CurrencyFormatter.formatRupiah(uiState.monthlyBudgetLimit - uiState.monthlyBudgetSpent)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isOver) StatusDanger else TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val progress = if (uiState.monthlyBudgetLimit > 0L) {
                            (uiState.monthlyBudgetSpent.toFloat() / uiState.monthlyBudgetLimit.toFloat()).coerceAtLeast(0f)
                        } else 0f

                        NeoProgressBar(
                            progress = progress,
                            isOverBudget = uiState.monthlyBudgetSpent > uiState.monthlyBudgetLimit,
                            height = 12.dp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${CurrencyFormatter.formatRupiah(uiState.monthlyBudgetSpent)} / ${CurrencyFormatter.formatRupiah(uiState.monthlyBudgetLimit)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            val percent = if (uiState.monthlyBudgetLimit > 0L) {
                                ((uiState.monthlyBudgetSpent.toDouble() / uiState.monthlyBudgetLimit.toDouble()) * 100).toInt()
                            } else 0
                            Text(
                                text = "$percent%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }

        if (uiState.activeGoal != null) {
            item {
                val goal = uiState.activeGoal!!
                val progress = if (goal.targetAmount > 0L) {
                    (goal.savedAmount.toFloat() / goal.targetAmount.toFloat()).coerceIn(0f, 1f)
                } else 0f
                val percent = (progress * 100).toInt()
                val remaining = (goal.targetAmount - goal.savedAmount).coerceAtLeast(0L)

                NeoCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Surface,
                    shadowOffset = 5.dp,
                    cornerRadius = 14.dp,
                    onClick = onNavigateToGoals
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(LightBlue)
                                        .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = NeoIcons.getGoalIcon(goal.icon),
                                        contentDescription = null,
                                        tint = BrightBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Target Tabungan",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = goal.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextMain
                                    )
                                }
                            }

                            Text(
                                text = if (goal.isAchieved) "Tercapai!" else "$percent%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (goal.isAchieved) StatusSuccess else BrightBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        NeoProgressBar(
                            progress = progress,
                            isOverBudget = false,
                            height = 12.dp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${CurrencyFormatter.formatRupiah(goal.savedAmount)} / ${CurrencyFormatter.formatRupiah(goal.targetAmount)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            Text(
                                text = if (goal.isAchieved) "Selesai" else "Sisa: ${CurrencyFormatter.formatRupiah(remaining)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaksi Terbaru",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
            }
        }

        item {
            AnimatedContent(
                targetState = uiState.recentTransactions.isEmpty(),
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(200))
                },
                label = "recentTxAnim"
            ) { isEmpty ->
                if (isEmpty) {
                    NeoEmptyState(
                        title = "Belum Ada Transaksi",
                        message = "Mulai catat pemasukan dan pengeluaran\nuntuk mengetahui kondisi keuanganmu.",
                        actionText = "Tambah Transaksi",
                        onActionClick = onNavigateToAddTransaction
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.recentTransactions.forEach { transaction ->
                            NeoTransactionItem(
                                item = transaction,
                                onClick = { onNavigateToTransactionDetail(transaction.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
