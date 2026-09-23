package com.kyu.tabungan.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoDialog
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.DateUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToAboutDev: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var pendingExportJson by remember { mutableStateOf<String?>(null) }
    var showClearDataDialog by remember { mutableStateOf(false) }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null && pendingExportJson != null) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        writer.write(pendingExportJson!!)
                    }
                }
                pendingExportJson = null
                snackbarHostState.currentSnackbarData?.dismiss()
            } catch (e: Exception) {
            }
        }
    }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        val content = reader.readText()
                        viewModel.onImportJsonSelected(content)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is SettingsEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                is SettingsEvent.ShareExportJson -> {
                    pendingExportJson = event.jsonString
                    createDocumentLauncher.launch(event.filename)
                }
            }
        }
    }

    if (uiState.isImportDialogVisible && uiState.pendingImportSummary != null) {
        val summary = uiState.pendingImportSummary!!
        NeoDialog(
            onDismissRequest = { viewModel.dismissImportDialog() },
            title = "Konfirmasi Impor Data",
            confirmText = "Pulihkan Sekarang",
            onConfirm = { viewModel.confirmImport() }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Berkas cadangan valid. Ringkasan data yang ditemukan:",
                    fontSize = 13.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                SummaryRow("Jumlah Dompet", "${summary.walletCount}")
                SummaryRow("Jumlah Kategori", "${summary.categoryCount}")
                SummaryRow("Jumlah Transaksi", "${summary.transactionCount}")
                SummaryRow("Jumlah Anggaran", "${summary.budgetCount}")
                SummaryRow("Waktu Cadangan", DateUtils.formatDateTime(summary.exportedAt))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Peringatan: Memulihkan data cadangan akan menggantikan data yang ada saat ini di aplikasi.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusDanger
                )
            }
        }
    }

    if (showClearDataDialog) {
        NeoDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = "Hapus Seluruh Data",
            confirmText = "Hapus Semua",
            confirmButtonColor = StatusDanger,
            onConfirm = {
                showClearDataDialog = false
                viewModel.clearAllData()
            }
        ) {
            Text(
                text = "Apakah Anda yakin ingin menghapus seluruh transaksi, dompet, dan anggaran? Data akan dikembalikan ke kondisi awal dan tidak dapat dikembalikan.",
                fontSize = 14.sp,
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NeoIconButton(
                        icon = NeoIcons.ChevronLeft,
                        onClick = onNavigateBack,
                        contentDescription = "Kembali",
                        size = 40.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Pengaturan",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                }
            }

            item {
                SectionHeader("Umum")
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
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Mata Uang",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            Text(
                                text = "Rupiah Indonesia (IDR)",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(LightBlue)
                                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Rp",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = TextMain
                            )
                        }
                    }
                }
            }

            item {
                SectionHeader("Data")
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SettingActionCard(
                        title = "Ekspor Data Cadangan",
                        description = "Simpan data keuangan ke berkas tabungan_backup.json",
                        icon = NeoIcons.Download,
                        onClick = { viewModel.exportData() }
                    )

                    SettingActionCard(
                        title = "Impor Data Cadangan",
                        description = "Pulihkan data keuangan dari berkas cadangan JSON",
                        icon = NeoIcons.Upload,
                        onClick = { openDocumentLauncher.launch(arrayOf("application/json", "*/*")) }
                    )

                    SettingActionCard(
                        title = "Muat Data Contoh",
                        description = "Isi data contoh untuk mencoba seluruh fitur aplikasi",
                        icon = NeoIcons.Check,
                        onClick = { viewModel.loadSampleData() }
                    )

                    SettingActionCard(
                        title = "Bersihkan Seluruh Data",
                        description = "Reset aplikasi dan hapus seluruh transaksi",
                        icon = NeoIcons.Trash,
                        iconColor = StatusDanger,
                        onClick = { showClearDataDialog = true }
                    )
                }
            }

            item {
                SectionHeader("Kategori")
            }

            item {
                SettingActionCard(
                    title = "Kelola Kategori",
                    description = "Atur kategori pemasukan dan pengeluaran",
                    icon = NeoIcons.Menu,
                    onClick = onNavigateToCategories
                )
            }

            item {
                SectionHeader("Tentang")
            }

            item {
                SettingActionCard(
                    title = "Tentang Pengembang",
                    description = "Profil pengembang Kyuu dan Saluran WhatsApp",
                    icon = NeoIcons.Info,
                    iconColor = BrightBlue,
                    onClick = onNavigateToAboutDev
                )
            }

            item {
                NeoCard(
                    modifier = Modifier.fillMaxWidth(),
                    shadowOffset = 4.dp,
                    cornerRadius = 12.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Versi Aplikasi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMain
                            )
                            Text(
                                text = "1.0.1",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(BorderColor.copy(alpha = 0.2f))
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Lisensi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMain
                            )
                            Text(
                                text = "MIT License",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrightBlue
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        color = TextMain,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun SettingActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color = TextMain,
    onClick: () -> Unit
) {
    NeoCard(
        modifier = Modifier.fillMaxWidth(),
        shadowOffset = 4.dp,
        cornerRadius = 12.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(LightBlue)
                    .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
            Icon(
                imageVector = NeoIcons.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextMuted)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextMain)
    }
}
