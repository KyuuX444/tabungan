package com.kyu.tabungan.ui.transaction

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.NeoAmountField
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.NeoTextField
import com.kyu.tabungan.components.NeoTransactionTypeToggle
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.DateUtils
import java.util.Calendar

@Composable
fun AddEditTransactionScreen(
    viewModel: AddEditTransactionViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is TransactionEvent.SaveSuccess -> onNavigateBack()
                is TransactionEvent.ShowToast -> {}
            }
        }
    }

    val cal = Calendar.getInstance().apply { timeInMillis = uiState.date }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            viewModel.setDate(selectedCal.timeInMillis)
        },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    )

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
                    text = if (uiState.isEditMode) "Edit Transaksi" else "Tambah Transaksi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
            }
        }

        item {
            NeoTransactionTypeToggle(
                selectedType = uiState.type,
                onTypeSelected = { viewModel.setType(it) }
            )
        }

        item {
            Column {
                Text(
                    text = "Nominal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                NeoAmountField(
                    amount = uiState.amount,
                    onAmountChange = { viewModel.setAmount(it) },
                    isExpense = uiState.type == TransactionType.EXPENSE,
                    errorMessage = uiState.amountError
                )
            }
        }

        item {
            Column {
                Text(
                    text = "Kategori",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (uiState.categoryError != null) {
                    Text(
                        text = uiState.categoryError!!,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusDanger,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                AnimatedContent(
                    targetState = uiState.type,
                    transitionSpec = {
                        val isGoingExpense = targetState == TransactionType.EXPENSE
                        (slideInHorizontally(animationSpec = tween(220, easing = FastOutSlowInEasing)) { width -> if (isGoingExpense) -width / 4 else width / 4 } +
                            fadeIn(animationSpec = tween(200)))
                            .togetherWith(
                                slideOutHorizontally(animationSpec = tween(180, easing = FastOutSlowInEasing)) { width -> if (isGoingExpense) width / 4 else -width / 4 } +
                                    fadeOut(animationSpec = tween(150))
                            )
                    },
                    label = "categoryAnim"
                ) { targetType ->
                    val currentTypeCategories = uiState.categories.filter { it.type == targetType }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(currentTypeCategories, key = { it.id }) { cat ->
                            val isSelected = uiState.selectedCategoryId == cat.id
                            val catIcon = NeoIcons.getCategoryIcon(cat.icon.ifEmpty { cat.name })
                            val catShape = RoundedCornerShape(10.dp)

                            Box(
                                modifier = Modifier.padding(end = 3.dp, bottom = 3.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .offset(x = 3.dp, y = 3.dp)
                                        .background(HardShadowColor, shape = catShape)
                                )
                                Row(
                                    modifier = Modifier
                                        .clip(catShape)
                                        .background(if (isSelected) BrightBlue else Surface)
                                        .border(width = 2.dp, color = BorderColor, shape = catShape)
                                        .clickable { viewModel.setCategory(cat.id) }
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = catIcon,
                                        contentDescription = cat.name,
                                        tint = if (isSelected) Surface else TextMain,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = cat.name,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Surface else TextMain
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Dompet",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (uiState.walletError != null) {
                    Text(
                        text = uiState.walletError!!,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusDanger,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.wallets, key = { it.id }) { wallet ->
                        val isSelected = uiState.selectedWalletId == wallet.id
                        val walletIcon = NeoIcons.getWalletIcon(wallet.icon)
                        val wShape = RoundedCornerShape(10.dp)

                        Box(
                            modifier = Modifier.padding(end = 3.dp, bottom = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .offset(x = 3.dp, y = 3.dp)
                                    .background(HardShadowColor, shape = wShape)
                            )
                            Row(
                                modifier = Modifier
                                    .clip(wShape)
                                    .background(if (isSelected) BrightBlue else Surface)
                                    .border(width = 2.dp, color = BorderColor, shape = wShape)
                                    .clickable { viewModel.setWallet(wallet.id) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = walletIcon,
                                    contentDescription = wallet.name,
                                    tint = if (isSelected) Surface else TextMain,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = wallet.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Surface else TextMain
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Tanggal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                val dateShape = RoundedCornerShape(10.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 3.dp, bottom = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .offset(x = 3.dp, y = 3.dp)
                            .background(HardShadowColor, shape = dateShape)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(dateShape)
                            .background(Surface)
                            .border(width = 2.dp, color = BorderColor, shape = dateShape)
                            .clickable { datePickerDialog.show() }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = NeoIcons.Calendar,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = DateUtils.formatDate(uiState.date),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }
                }
            }
        }

        item {
            NeoTextField(
                value = uiState.note,
                onValueChange = { viewModel.setNote(it) },
                label = "Catatan (Opsional)"
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            NeoButton(
                text = if (uiState.isEditMode) "Perbarui Transaksi" else "Simpan Transaksi",
                onClick = { viewModel.saveTransaction() },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = BrightBlue,
                contentColor = Surface,
                icon = NeoIcons.Check
            )
        }
    }
}
