package com.kyu.tabungan.ui.budget

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.NeoAmountField
import com.kyu.tabungan.components.NeoBudgetCard
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoDialog
import com.kyu.tabungan.components.NeoEmptyState
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.NeoProgressBar
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.model.BudgetWithUsage
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
import com.kyu.tabungan.util.CurrencyFormatter
import com.kyu.tabungan.util.DateUtils

@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingBudget by remember { mutableStateOf<BudgetWithUsage?>(null) }
    var deletingBudgetId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is BudgetEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    if (showAddEditDialog) {
        var selectedCatId by remember {
            mutableStateOf(editingBudget?.categoryId ?: uiState.expenseCategories.firstOrNull()?.id ?: 1L)
        }
        var limitAmount by remember {
            mutableStateOf(editingBudget?.limitAmount ?: 500000L)
        }

        NeoDialog(
            onDismissRequest = {
                showAddEditDialog = false
                editingBudget = null
            },
            title = if (editingBudget != null) "Edit Anggaran" else "Tambah Anggaran",
            confirmText = "Simpan",
            onConfirm = {
                viewModel.saveBudget(
                    id = editingBudget?.id ?: 0L,
                    categoryId = selectedCatId,
                    limitAmount = limitAmount
                )
                showAddEditDialog = false
                editingBudget = null
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (editingBudget == null) {
                    Column {
                        Text(
                            text = "Kategori Pengeluaran",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(uiState.expenseCategories, key = { it.id }) { cat ->
                                val isSelected = selectedCatId == cat.id
                                val catShape = RoundedCornerShape(8.dp)
                                val icon = NeoIcons.getCategoryIcon(cat.icon.ifEmpty { cat.name })

                                Box(modifier = Modifier.padding(end = 2.dp, bottom = 2.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .offset(x = 2.dp, y = 2.dp)
                                            .background(HardShadowColor, shape = catShape)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .clip(catShape)
                                            .background(if (isSelected) BrightBlue else Surface)
                                            .border(width = 1.5.dp, color = BorderColor, shape = catShape)
                                            .clickable { selectedCatId = cat.id }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = cat.name,
                                            tint = if (isSelected) Surface else TextMain,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = cat.name,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Surface else TextMain
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Kategori: ${editingBudget?.categoryName}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                }

                Column {
                    Text(
                        text = "Batas Anggaran Bulanan",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    NeoAmountField(
                        amount = limitAmount,
                        onAmountChange = { limitAmount = it },
                        isExpense = false
                    )
                }
            }
        }
    }

    if (deletingBudgetId != null) {
        NeoDialog(
            onDismissRequest = { deletingBudgetId = null },
            title = "Hapus Anggaran",
            confirmText = "Hapus",
            confirmButtonColor = StatusDanger,
            onConfirm = {
                val idToDelete = deletingBudgetId!!
                deletingBudgetId = null
                viewModel.deleteBudget(idToDelete)
            }
        ) {
            Text(
                text = "Apakah Anda yakin ingin menghapus target anggaran untuk kategori ini?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextMain
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        NeoIconButton(
                            icon = NeoIcons.ChevronLeft,
                            onClick = onNavigateBack,
                            contentDescription = "Kembali",
                            size = 40.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = DateUtils.formatMonthYear(uiState.month, uiState.year),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = "Anggaran",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextMain
                            )
                        }
                    }

                    NeoButton(
                        text = "Tambah",
                        onClick = {
                            editingBudget = null
                            showAddEditDialog = true
                        },
                        icon = NeoIcons.Plus,
                        shadowOffset = 3.dp,
                        cornerRadius = 10.dp
                    )
                }
            }

            item {
                val remaining = uiState.totalLimit - uiState.totalSpent
                val isOver = uiState.totalSpent > uiState.totalLimit
                val totalProgress = if (uiState.totalLimit > 0L) {
                    (uiState.totalSpent.toFloat() / uiState.totalLimit.toFloat()).coerceAtLeast(0f)
                } else 0f

                NeoCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = if (isOver) Color(0xFFFFECEC) else Surface,
                    shadowOffset = 6.dp,
                    cornerRadius = 16.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "TOTAL ANGGARAN BULANAN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = CurrencyFormatter.formatRupiah(uiState.totalLimit),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = TextMain
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        NeoProgressBar(
                            progress = totalProgress,
                            isOverBudget = isOver,
                            height = 14.dp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Terpakai",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextMuted
                                )
                                Text(
                                    text = CurrencyFormatter.formatRupiah(uiState.totalSpent),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isOver) StatusDanger else TextMain
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (isOver) "Melebihi" else "Sisa",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOver) StatusDanger else TextMuted
                                )
                                Text(
                                    text = CurrencyFormatter.formatRupiah(if (isOver) uiState.totalSpent - uiState.totalLimit else remaining),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isOver) StatusDanger else StatusSuccess
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Anggaran per Kategori",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
            }

            if (uiState.budgets.isEmpty()) {
                item {
                    NeoEmptyState(
                        title = "Belum Ada Target Anggaran",
                        message = "Buat target anggaran per kategori untuk mengontrol pengeluaran bulanan Anda.",
                        actionText = "Tambah Anggaran",
                        onActionClick = {
                            editingBudget = null
                            showAddEditDialog = true
                        }
                    )
                }
            } else {
                items(uiState.budgets, key = { it.id }) { budgetItem ->
                    NeoBudgetCard(
                        budget = budgetItem,
                        onEdit = {
                            editingBudget = budgetItem
                            showAddEditDialog = true
                        },
                        onDelete = {
                            deletingBudgetId = budgetItem.id
                        }
                    )
                }
            }
        }
    }
}
