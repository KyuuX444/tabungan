package com.kyu.tabungan.ui.wallet

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoDialog
import com.kyu.tabungan.components.NeoTextField
import com.kyu.tabungan.components.NeoWalletCard
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.WalletType
import com.kyu.tabungan.data.model.WalletWithBalance
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter

@Composable
fun WalletScreen(
    viewModel: WalletViewModel,
    modifier: Modifier = Modifier
) {
    val wallets by viewModel.walletsWithBalance.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingWallet by remember { mutableStateOf<WalletWithBalance?>(null) }
    var deletingWalletId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is WalletEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    if (showAddEditDialog) {
        var walletName by remember { mutableStateOf(editingWallet?.name.orEmpty()) }
        var walletType by remember { mutableStateOf(editingWallet?.type ?: WalletType.CASH) }
        var initialBalance by remember { mutableStateOf(editingWallet?.initialBalance ?: 0L) }

        NeoDialog(
            onDismissRequest = {
                showAddEditDialog = false
                editingWallet = null
            },
            title = if (editingWallet != null) "Edit Dompet" else "Tambah Dompet",
            confirmText = "Simpan",
            onConfirm = {
                val iconName = when (walletType) {
                    WalletType.CASH -> "cash"
                    WalletType.BANK -> "bank"
                    WalletType.EWALLET -> "ewallet"
                    WalletType.OTHER -> "wallet"
                }
                viewModel.saveWallet(
                    id = editingWallet?.id ?: 0L,
                    name = walletName,
                    type = walletType,
                    initialBalance = initialBalance,
                    icon = iconName
                )
                showAddEditDialog = false
                editingWallet = null
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                NeoTextField(
                    value = walletName,
                    onValueChange = { walletName = it },
                    label = "Nama Dompet"
                )

                Column {
                    Text(
                        text = "Jenis Dompet",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WalletType.values().forEach { type ->
                            val isSelected = walletType == type
                            val typeLabel = when (type) {
                                WalletType.CASH -> "Tunai"
                                WalletType.BANK -> "Bank"
                                WalletType.EWALLET -> "E-Wallet"
                                WalletType.OTHER -> "Lainnya"
                            }
                            val tShape = RoundedCornerShape(8.dp)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 2.dp, bottom = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .offset(x = 2.dp, y = 2.dp)
                                        .background(HardShadowColor, shape = tShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(tShape)
                                        .background(if (isSelected) BrightBlue else Surface)
                                        .border(width = 1.5.dp, color = BorderColor, shape = tShape)
                                        .clickable { walletType = type }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = typeLabel,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Surface else TextMain
                                    )
                                }
                            }
                        }
                    }
                }

                if (editingWallet == null) {
                    Column {
                        Text(
                            text = "Saldo Awal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        NeoAmountField(
                            amount = initialBalance,
                            onAmountChange = { initialBalance = it },
                            isExpense = false
                        )
                    }
                }
            }
        }
    }

    if (deletingWalletId != null) {
        NeoDialog(
            onDismissRequest = { deletingWalletId = null },
            title = "Hapus Dompet",
            confirmText = "Hapus",
            confirmButtonColor = StatusDanger,
            onConfirm = {
                val idToDelete = deletingWalletId!!
                deletingWalletId = null
                viewModel.deleteWallet(idToDelete)
            }
        ) {
            Text(
                text = "Apakah Anda yakin ingin menghapus dompet ini? Dompet yang memiliki transaksi tidak dapat dihapus untuk mencegah kerusakan data.",
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Kelola Keuangan",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Text(
                            text = "Dompet Saya",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextMain
                        )
                    }

                    NeoButton(
                        text = "Tambah",
                        onClick = {
                            editingWallet = null
                            showAddEditDialog = true
                        },
                        icon = NeoIcons.Plus,
                        shadowOffset = 3.dp,
                        cornerRadius = 10.dp
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
                            text = "TOTAL SALDO SEMUA DOMPET",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Surface.copy(alpha = 0.9f),
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = CurrencyFormatter.formatRupiah(totalBalance),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Surface
                        )
                    }
                }
            }

            items(wallets, key = { it.id }) { walletItem ->
                NeoWalletCard(
                    walletWithBalance = walletItem,
                    onEdit = {
                        editingWallet = walletItem
                        showAddEditDialog = true
                    },
                    onDelete = {
                        deletingWalletId = walletItem.id
                    }
                )
            }
        }
    }
}
