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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.model.BudgetWithUsage
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter

@Composable
fun NeoBudgetCard(
    budget: BudgetWithUsage,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = NeoIcons.getCategoryIcon(budget.categoryIcon.ifEmpty { budget.categoryName })
    val isOver = budget.isOverBudget

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
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isOver) Color(0xFFFFE5E5) else LightBlue)
                            .border(
                                width = 2.dp,
                                color = if (isOver) StatusDanger else BorderColor,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = budget.categoryName,
                            tint = if (isOver) StatusDanger else TextMain,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = budget.categoryName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                        if (isOver) {
                            Text(
                                text = "Melebihi Anggaran",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusDanger
                            )
                        } else {
                            Text(
                                text = "Sisa: ${CurrencyFormatter.formatRupiah(budget.remainingAmount)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMuted
                            )
                        }
                    }
                }

                Row {
                    NeoIconButton(
                        icon = NeoIcons.Edit,
                        onClick = onEdit,
                        contentDescription = "Edit Anggaran",
                        size = 34.dp,
                        shadowOffset = 2.dp,
                        borderWidth = 1.5.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    NeoIconButton(
                        icon = NeoIcons.Trash,
                        onClick = onDelete,
                        contentDescription = "Hapus Anggaran",
                        size = 34.dp,
                        shadowOffset = 2.dp,
                        borderWidth = 1.5.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            NeoProgressBar(
                progress = budget.progress,
                isOverBudget = isOver,
                height = 12.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${CurrencyFormatter.formatRupiah(budget.spentAmount)} / ${CurrencyFormatter.formatRupiah(budget.limitAmount)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOver) StatusDanger else TextMain
                )
                Text(
                    text = "${budget.percentageInt}%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOver) StatusDanger else TextMuted
                )
            }
        }
    }
}
