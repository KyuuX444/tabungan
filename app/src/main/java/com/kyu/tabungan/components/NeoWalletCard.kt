package com.kyu.tabungan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.WalletType
import com.kyu.tabungan.data.model.WalletWithBalance
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter

@Composable
fun NeoWalletCard(
    walletWithBalance: WalletWithBalance,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = NeoIcons.getWalletIcon(walletWithBalance.icon)
    val typeLabel = when (walletWithBalance.type) {
        WalletType.CASH -> "Tunai"
        WalletType.BANK -> "Bank"
        WalletType.EWALLET -> "E-Wallet"
        WalletType.OTHER -> "Lainnya"
    }

    NeoCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Surface,
        shadowOffset = 5.dp,
        borderWidth = 2.5.dp,
        cornerRadius = 14.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            contentDescription = walletWithBalance.name,
                            tint = BrightBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = walletWithBalance.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(LightBlue)
                                .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = typeLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                        }
                    }
                }

                Row {
                    NeoIconButton(
                        icon = NeoIcons.Edit,
                        onClick = onEdit,
                        contentDescription = "Edit Dompet",
                        size = 36.dp,
                        shadowOffset = 2.dp,
                        borderWidth = 1.5.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NeoIconButton(
                        icon = NeoIcons.Trash,
                        onClick = onDelete,
                        contentDescription = "Hapus Dompet",
                        size = 36.dp,
                        shadowOffset = 2.dp,
                        borderWidth = 1.5.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Saldo Dompet",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                    Text(
                        text = CurrencyFormatter.formatRupiah(walletWithBalance.currentBalance),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Saldo Awal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                    Text(
                        text = CurrencyFormatter.formatRupiah(walletWithBalance.initialBalance),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
