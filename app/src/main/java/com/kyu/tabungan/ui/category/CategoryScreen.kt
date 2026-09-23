package com.kyu.tabungan.ui.category

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
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoCategoryTabToggle
import com.kyu.tabungan.components.NeoDialog
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.NeoTextField
import com.kyu.tabungan.components.NeoTransactionTypeToggle
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.CategoryEntity
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.allCategories.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by remember { mutableStateOf(TransactionType.EXPENSE) }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    var deletingCategoryId by remember { mutableStateOf<Long?>(null) }

    val availableIcons = listOf(
        "food", "transport", "shopping", "entertainment", "bills",
        "education", "health", "salary", "freelance", "business", "gift", "other"
    )

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is CategoryEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    if (showAddEditDialog) {
        var catName by remember { mutableStateOf(editingCategory?.name.orEmpty()) }
        var catType by remember { mutableStateOf(editingCategory?.type ?: selectedTab) }
        var selectedIcon by remember { mutableStateOf(editingCategory?.icon ?: "other") }

        NeoDialog(
            onDismissRequest = {
                showAddEditDialog = false
                editingCategory = null
            },
            title = if (editingCategory != null) "Edit Kategori" else "Tambah Kategori",
            confirmText = "Simpan",
            onConfirm = {
                viewModel.saveCategory(
                    id = editingCategory?.id ?: 0L,
                    name = catName,
                    type = catType,
                    icon = selectedIcon
                )
                showAddEditDialog = false
                editingCategory = null
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                NeoTextField(
                    value = catName,
                    onValueChange = { catName = it },
                    label = "Nama Kategori",
                    placeholder = "Contoh: Kopi, Asuransi, Hobi"
                )

                Column {
                    Text(
                        text = "Jenis Transaksi",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    NeoTransactionTypeToggle(
                        selectedType = catType,
                        onTypeSelected = { catType = it }
                    )
                }

                Column {
                    Text(
                        text = "Pilih Ikon",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableIcons) { iconName ->
                            val isSelected = selectedIcon == iconName
                            val iconVector = NeoIcons.getCategoryIcon(iconName)
                            val iconShape = RoundedCornerShape(8.dp)

                            Box(modifier = Modifier.padding(end = 2.dp, bottom = 2.dp)) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .offset(x = 2.dp, y = 2.dp)
                                        .background(HardShadowColor, shape = iconShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(iconShape)
                                        .background(if (isSelected) BrightBlue else Surface)
                                        .border(width = 1.5.dp, color = BorderColor, shape = iconShape)
                                        .clickable { selectedIcon = iconName },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = iconVector,
                                        contentDescription = iconName,
                                        tint = if (isSelected) Surface else TextMain,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (deletingCategoryId != null) {
        NeoDialog(
            onDismissRequest = { deletingCategoryId = null },
            title = "Hapus Kategori",
            confirmText = "Hapus",
            confirmButtonColor = StatusDanger,
            onConfirm = {
                val idToDelete = deletingCategoryId!!
                deletingCategoryId = null
                viewModel.deleteCategory(idToDelete)
            }
        ) {
            Text(
                text = "Apakah Anda yakin ingin menghapus kategori ini? Kategori yang memiliki transaksi tidak dapat dihapus untuk melindungi integritas data.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextMain
            )
        }
    }

    val filteredList = categories.filter { it.type == selectedTab }

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
                                text = "Pengaturan",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = "Kelola Kategori",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextMain
                            )
                        }
                    }

                    NeoButton(
                        text = "Tambah",
                        onClick = {
                            editingCategory = null
                            showAddEditDialog = true
                        },
                        icon = NeoIcons.Plus,
                        shadowOffset = 3.dp,
                        cornerRadius = 10.dp
                    )
                }
            }

            item {
                NeoCategoryTabToggle(
                    selectedType = selectedTab,
                    onTypeSelected = { selectedTab = it }
                )
            }

            items(filteredList, key = { it.id }) { cat ->
                val icon = NeoIcons.getCategoryIcon(cat.icon.ifEmpty { cat.name })

                NeoCard(
                    modifier = Modifier.fillMaxWidth(),
                    shadowOffset = 4.dp,
                    cornerRadius = 12.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                    contentDescription = cat.name,
                                    tint = TextMain,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = cat.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                        }

                        Row {
                            NeoIconButton(
                                icon = NeoIcons.Edit,
                                onClick = {
                                    editingCategory = cat
                                    showAddEditDialog = true
                                },
                                contentDescription = "Edit Kategori",
                                size = 34.dp,
                                shadowOffset = 2.dp,
                                borderWidth = 1.5.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            NeoIconButton(
                                icon = NeoIcons.Trash,
                                onClick = {
                                    deletingCategoryId = cat.id
                                },
                                contentDescription = "Hapus Kategori",
                                size = 34.dp,
                                shadowOffset = 2.dp,
                                borderWidth = 1.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
