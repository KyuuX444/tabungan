package com.kyu.tabungan.ui.more

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted

@Composable
fun MoreScreen(
    onNavigateToGoals: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAboutDev: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Menu Tambahan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
                Text(
                    text = "Lainnya",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            MoreMenuCard(
                title = "Target Tabungan",
                subtitle = "Rencanakan impian beli HP, liburan, dan target harian",
                icon = NeoIcons.Goal,
                onClick = onNavigateToGoals
            )
        }

        item {
            MoreMenuCard(
                title = "Anggaran Bulanan",
                subtitle = "Kelola target batas pengeluaran per kategori",
                icon = NeoIcons.Budget,
                onClick = onNavigateToBudgets
            )
        }

        item {
            MoreMenuCard(
                title = "Cari Transaksi",
                subtitle = "Filter dan cari riwayat transaksi keuangan",
                icon = NeoIcons.Search,
                onClick = onNavigateToSearch
            )
        }

        item {
            MoreMenuCard(
                title = "Kelola Kategori",
                subtitle = "Tambah, ubah, dan hapus kategori transaksi",
                icon = NeoIcons.Menu,
                onClick = onNavigateToCategories
            )
        }

        item {
            MoreMenuCard(
                title = "Pengaturan & Notifikasi",
                subtitle = "Pengingat nabung harian, cadangan data, dan sistem",
                icon = NeoIcons.Settings,
                onClick = onNavigateToSettings
            )
        }

        item {
            MoreMenuCard(
                title = "Tentang Pengembang",
                subtitle = "Profil pengembang Kyuu, Saluran WA, dan YouTube",
                icon = NeoIcons.Info,
                onClick = onNavigateToAboutDev
            )
        }
    }
}

@Composable
private fun MoreMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val shadowOffset = 5.dp
    val currentOffset by animateDpAsState(
        targetValue = if (isPressed) 3.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "moreCardPress"
    )
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = shadowOffset, bottom = shadowOffset)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(HardShadowColor, shape = shape)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = currentOffset, y = currentOffset)
                .clip(shape)
                .background(Surface)
                .border(width = 2.dp, color = BorderColor, shape = shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(LightBlue)
                        .border(width = 2.dp, color = BorderColor, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = BrightBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Icon(
                    imageVector = NeoIcons.ChevronRight,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
