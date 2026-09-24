package com.kyu.tabungan.ui.goal

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import java.io.File
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kyu.tabungan.components.NeoAmountField
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoDialog
import com.kyu.tabungan.components.NeoEmptyState
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.NeoProgressBar
import com.kyu.tabungan.components.NeoTextField
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.SavingsGoalEntity
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
import kotlin.math.ceil

@Composable
fun SavingsGoalScreen(
    viewModel: SavingsGoalViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.allGoals.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf<SavingsGoalEntity?>(null) }
    var deletingGoalId by remember { mutableStateOf<Long?>(null) }
    var addingSavingsGoal by remember { mutableStateOf<SavingsGoalEntity?>(null) }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is SavingsGoalEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    if (showAddEditDialog) {
        var goalName by remember { mutableStateOf(editingGoal?.name.orEmpty()) }
        var targetAmount by remember { mutableStateOf(editingGoal?.targetAmount ?: 0L) }
        var savedAmount by remember { mutableStateOf(editingGoal?.savedAmount ?: 0L) }
        var dailyTarget by remember { mutableStateOf(editingGoal?.dailyTarget ?: 0L) }
        var imageUriString by remember { mutableStateOf(editingGoal?.imageUri) }
        var selectedIcon by remember { mutableStateOf(editingGoal?.icon ?: "phone") }
        var note by remember { mutableStateOf(editingGoal?.note.orEmpty()) }

        val context = LocalContext.current
        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                try {
                    val goalsDir = File(context.filesDir, "goals").apply { mkdirs() }
                    val destFile = File(goalsDir, "goal_${System.currentTimeMillis()}.jpg")
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        destFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    imageUriString = Uri.fromFile(destFile).toString()
                } catch (e: Exception) {
                    imageUriString = uri.toString()
                }
            }
        }

        val availableIcons = listOf(
            "phone", "laptop", "motor", "house", "camera", "trip", "gold", "other"
        )

        val remaining = (targetAmount - savedAmount).coerceAtLeast(0L)
        val estimatedDays = if (dailyTarget > 0L && remaining > 0L) {
            ceil(remaining.toDouble() / dailyTarget.toDouble()).toLong()
        } else {
            0L
        }

        NeoDialog(
            onDismissRequest = {
                showAddEditDialog = false
                editingGoal = null
            },
            title = if (editingGoal != null) "Edit Target Tabungan" else "Tambah Target Tabungan",
            confirmText = "Simpan",
            onConfirm = {
                viewModel.saveGoal(
                    id = editingGoal?.id ?: 0L,
                    name = goalName,
                    targetAmount = targetAmount,
                    savedAmount = savedAmount,
                    dailyTarget = dailyTarget,
                    imageUri = imageUriString,
                    icon = selectedIcon,
                    note = note
                )
                showAddEditDialog = false
                editingGoal = null
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Column {
                    Text(
                        text = "Foto Impian (Opsional)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val photoShape = RoundedCornerShape(10.dp)
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(photoShape)
                                .background(LightBlue)
                                .border(width = 2.dp, color = BorderColor, shape = photoShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageUriString != null) {
                                AsyncImage(
                                    model = imageUriString,
                                    contentDescription = "Foto Target",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = NeoIcons.getGoalIcon(selectedIcon),
                                    contentDescription = null,
                                    tint = BrightBlue,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            NeoButton(
                                text = if (imageUriString != null) "Ganti Foto" else "Pilih Foto",
                                onClick = { imagePickerLauncher.launch("image/*") },
                                shadowOffset = 2.dp,
                                borderWidth = 1.5.dp,
                                cornerRadius = 8.dp
                            )
                            if (imageUriString != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Hapus Foto",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusDanger,
                                    modifier = Modifier
                                        .clickable { imageUriString = null }
                                        .padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                NeoTextField(
                    value = goalName,
                    onValueChange = { goalName = it },
                    label = "Nama Target"
                )

                Column {
                    Text(
                        text = "Total Target Nominal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    NeoAmountField(
                        amount = targetAmount,
                        onAmountChange = { targetAmount = it },
                        isExpense = false
                    )
                }

                Column {
                    Text(
                        text = "Tabungan Per Hari",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    NeoAmountField(
                        amount = dailyTarget,
                        onAmountChange = { dailyTarget = it },
                        isExpense = false
                    )
                }

                Column {
                    Text(
                        text = "Saldo Awal Terkumpul",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    NeoAmountField(
                        amount = savedAmount,
                        onAmountChange = { savedAmount = it },
                        isExpense = false
                    )
                }

                if (dailyTarget > 0L && targetAmount > 0L) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(LightBlue)
                            .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "Estimasi Tercapai:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = "$estimatedDays hari lagi (${DateUtils.formatDate(System.currentTimeMillis() + estimatedDays * 86400000L)})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = BrightBlue
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = "Pilih Ikon Cadangan",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableIcons) { iconName ->
                            val isSelected = selectedIcon == iconName
                            val iconShape = RoundedCornerShape(8.dp)
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(iconShape)
                                    .background(if (isSelected) BrightBlue else Surface)
                                    .border(width = 2.dp, color = BorderColor, shape = iconShape)
                                    .clickable { selectedIcon = iconName },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = NeoIcons.getGoalIcon(iconName),
                                    contentDescription = iconName,
                                    tint = if (isSelected) Surface else TextMain,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                NeoTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = "Catatan (Opsional)"
                )
            }
        }
    }

    if (addingSavingsGoal != null) {
        val current = addingSavingsGoal!!
        var addAmount by remember { mutableStateOf(if (current.dailyTarget > 0L) current.dailyTarget else 50000L) }

        NeoDialog(
            onDismissRequest = { addingSavingsGoal = null },
            title = "Nabung ke ${current.name}",
            confirmText = "Tambah Tabungan",
            onConfirm = {
                viewModel.addSavings(current.id, addAmount)
                addingSavingsGoal = null
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Masukkan nominal uang yang ingin disisihkan ke target ini:",
                    fontSize = 13.sp,
                    color = TextMuted
                )
                NeoAmountField(
                    amount = addAmount,
                    onAmountChange = { addAmount = it },
                    isExpense = false
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(20000L, 50000L, 100000L).forEach { quickAmount ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(LightBlue)
                                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(6.dp))
                                .clickable { addAmount = quickAmount }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "+${CurrencyFormatter.formatRupiah(quickAmount)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrightBlue
                            )
                        }
                    }
                }
            }
        }
    }

    if (deletingGoalId != null) {
        NeoDialog(
            onDismissRequest = { deletingGoalId = null },
            title = "Hapus Target Tabungan",
            confirmText = "Hapus",
            confirmButtonColor = StatusDanger,
            onConfirm = {
                val idToDelete = deletingGoalId!!
                deletingGoalId = null
                viewModel.deleteGoal(idToDelete)
            }
        ) {
            Text(
                text = "Apakah Anda yakin ingin menghapus target tabungan ini? Data yang dihapus tidak dapat dipulihkan.",
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
                                text = "Impian & Tabungan",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = "Target Tabungan",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextMain
                            )
                        }
                    }

                    NeoButton(
                        text = "Tambah",
                        onClick = {
                            editingGoal = null
                            showAddEditDialog = true
                        },
                        icon = NeoIcons.Plus,
                        shadowOffset = 3.dp,
                        cornerRadius = 10.dp
                    )
                }
            }

            if (goals.isEmpty()) {
                item {
                    NeoEmptyState(
                        title = "Belum Ada Target Tabungan",
                        message = "Wujudkan impianmu seperti beli HP, kendaraan, atau liburan dengan target harian yang teratur.",
                        actionText = "Tambah Target",
                        onActionClick = {
                            editingGoal = null
                            showAddEditDialog = true
                        }
                    )
                }
            } else {
                items(goals, key = { it.id }) { goal ->
                    GoalCardItem(
                        goal = goal,
                        onAddSavings = { addingSavingsGoal = goal },
                        onEdit = {
                            editingGoal = goal
                            showAddEditDialog = true
                        },
                        onDelete = { deletingGoalId = goal.id }
                    )
                }
            }
        }
    }
}

@Composable
private fun GoalCardItem(
    goal: SavingsGoalEntity,
    onAddSavings: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = if (goal.targetAmount > 0L) {
        (goal.savedAmount.toFloat() / goal.targetAmount.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val percent = (progress * 100).toInt()
    val remainingAmount = (goal.targetAmount - goal.savedAmount).coerceAtLeast(0L)
    val remainingDays = if (goal.dailyTarget > 0L && remainingAmount > 0L) {
        ceil(remainingAmount.toDouble() / goal.dailyTarget.toDouble()).toLong()
    } else 0L

    NeoCard(
        modifier = Modifier.fillMaxWidth(),
        shadowOffset = 5.dp,
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val thumbShape = RoundedCornerShape(12.dp)
                Box(
                    modifier = Modifier.padding(end = 2.dp, bottom = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .offset(x = 3.dp, y = 3.dp)
                            .background(HardShadowColor, shape = thumbShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(thumbShape)
                            .background(LightBlue)
                            .border(width = 2.dp, color = BorderColor, shape = thumbShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (goal.imageUri != null) {
                            AsyncImage(
                                model = goal.imageUri,
                                contentDescription = goal.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = NeoIcons.getGoalIcon(goal.icon),
                                contentDescription = null,
                                tint = BrightBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (goal.isAchieved) Color(0xFFE8F9EE) else LightBlue)
                            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (goal.isAchieved) "Tercapai!" else "Dalam Proses",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (goal.isAchieved) StatusSuccess else BrightBlue
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    NeoIconButton(
                        icon = NeoIcons.Edit,
                        onClick = onEdit,
                        contentDescription = "Edit",
                        size = 36.dp
                    )
                    NeoIconButton(
                        icon = NeoIcons.Trash,
                        onClick = onDelete,
                        contentDescription = "Hapus",
                        size = 36.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

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
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Text(
                    text = "$percent%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = BrightBlue
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Background)
                    .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Nabung Per Hari",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (goal.dailyTarget > 0L) "${CurrencyFormatter.formatRupiah(goal.dailyTarget)} / hari" else "Fleksibel",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (goal.isAchieved) "Status" else "Estimasi Sisa Hari",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (goal.isAchieved) "Target Terpenuhi" else if (remainingDays > 0L) "$remainingDays hari lagi" else "Belum diset",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (goal.isAchieved) StatusSuccess else TextMain
                        )
                    }
                }
            }

            if (!goal.isAchieved) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    NeoButton(
                        text = "+ Nabung",
                        onClick = onAddSavings,
                        backgroundColor = Color(0xFF19B96B),
                        shadowOffset = 3.dp,
                        borderWidth = 1.5.dp,
                        cornerRadius = 8.dp
                    )
                }
            }
        }
    }
}
