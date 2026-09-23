package com.kyu.tabungan.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted

@Composable
fun TabunganBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .border(width = 2.5.dp, color = BorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem(
                title = "Beranda",
                icon = NeoIcons.Home,
                isSelected = currentRoute == Screen.Home.route,
                onClick = { onNavigate(Screen.Home.route) }
            )

            BottomNavItem(
                title = "Statistik",
                icon = NeoIcons.Chart,
                isSelected = currentRoute == Screen.Statistics.route,
                onClick = { onNavigate(Screen.Statistics.route) }
            )

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .padding(bottom = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .offset(x = 3.dp, y = 3.dp)
                        .background(color = HardShadowColor, shape = CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(BrightBlue)
                        .border(width = 2.5.dp, color = BorderColor, shape = CircleShape)
                        .clickable(onClick = onAddClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = NeoIcons.Plus,
                        contentDescription = "Tambah Transaksi",
                        tint = Surface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            BottomNavItem(
                title = "Dompet",
                icon = NeoIcons.Wallet,
                isSelected = currentRoute == Screen.Wallets.route,
                onClick = { onNavigate(Screen.Wallets.route) }
            )

            BottomNavItem(
                title = "Lainnya",
                icon = NeoIcons.Menu,
                isSelected = currentRoute == Screen.More.route ||
                        currentRoute == Screen.Budgets.route ||
                        currentRoute == Screen.Categories.route ||
                        currentRoute == Screen.Settings.route,
                onClick = { onNavigate(Screen.More.route) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val activeColor = BrightBlue
    val inactiveColor = TextMuted

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
            color = if (isSelected) TextMain else inactiveColor,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
