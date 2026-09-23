package com.kyu.tabungan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.data.entity.TransactionType
import com.kyu.tabungan.data.model.TransactionItemModel
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.StatusSuccess
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter
import com.kyu.tabungan.util.DateUtils

@Composable
fun NeoTransactionItem(
    item: TransactionItemModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIncome = item.type == TransactionType.INCOME
    val amountColor = if (isIncome) StatusSuccess else StatusDanger
    val icon = NeoIcons.getCategoryIcon(item.categoryIcon.ifEmpty { item.categoryName })

    NeoCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shadowOffset = 4.dp,
        borderWidth = 2.dp,
        cornerRadius = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(LightBlue)
                    .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = item.categoryName,
                    tint = TextMain,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.categoryName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!item.note.isNullOrBlank()) {
                    Text(
                        text = item.note,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = DateUtils.formatShortDate(item.date),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                    Text(
                        text = " • ",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(LightBlue)
                            .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = item.walletName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMain
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = CurrencyFormatter.formatRupiahSigned(item.amount, isIncome),
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = amountColor
            )
        }
    }
}
