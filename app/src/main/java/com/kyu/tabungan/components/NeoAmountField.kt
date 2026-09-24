package com.kyu.tabungan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.StatusDanger
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted
import com.kyu.tabungan.util.CurrencyFormatter

@Composable
fun NeoAmountField(
    amount: Long,
    onAmountChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isExpense: Boolean = true,
    errorMessage: String? = null,
    shadowOffset: Dp = 4.dp
) {
    val shape = RoundedCornerShape(14.dp)
    val hasError = !errorMessage.isNullOrBlank()

    val formattedString = if (amount == 0L) "" else CurrencyFormatter.formatRupiah(amount, withPrefix = false)

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = shadowOffset, bottom = shadowOffset)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = shadowOffset, y = shadowOffset)
                    .background(
                        color = if (hasError) StatusDanger else HardShadowColor,
                        shape = shape
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(Surface)
                    .border(
                        width = 2.dp,
                        color = if (hasError) StatusDanger else BorderColor,
                        shape = shape
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isExpense) Color(0xFFFFECEC) else Color(0xFFE8F9EE))
                            .border(
                                width = 1.5.dp,
                                color = BorderColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Rp",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isExpense) Color(0xFFFF4D4D) else Color(0xFF19B96B)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    BasicTextField(
                        value = formattedString,
                        onValueChange = { input ->
                            val parsed = CurrencyFormatter.parseAmount(input)
                            onAmountChange(parsed)
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = TextMain,
                            textAlign = TextAlign.Start
                        ),
                        cursorBrush = SolidColor(BorderColor),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { innerTextField ->
                            innerTextField()
                        }
                    )
                }
            }
        }

        if (hasError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage.orEmpty(),
                color = StatusDanger,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
