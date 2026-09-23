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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain

@Composable
fun NeoDialog(
    onDismissRequest: () -> Unit,
    title: String,
    confirmText: String = "Simpan",
    onConfirm: () -> Unit,
    cancelText: String? = "Batal",
    onCancel: (() -> Unit)? = onDismissRequest,
    confirmButtonColor: Color = BrightBlue,
    confirmButtonTextColor: Color = Surface,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val shadowOffset = 6.dp

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(end = shadowOffset, bottom = shadowOffset)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = shadowOffset, y = shadowOffset)
                    .background(color = HardShadowColor, shape = shape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(Surface)
                    .border(width = 2.5.dp, color = BorderColor, shape = shape)
                    .padding(20.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )

                Spacer(modifier = Modifier.height(16.dp))

                content()

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (cancelText != null && onCancel != null) {
                        NeoButton(
                            text = cancelText,
                            onClick = onCancel,
                            backgroundColor = Surface,
                            contentColor = TextMain,
                            shadowOffset = 3.dp,
                            borderWidth = 2.dp,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }

                    NeoButton(
                        text = confirmText,
                        onClick = onConfirm,
                        backgroundColor = confirmButtonColor,
                        contentColor = confirmButtonTextColor,
                        shadowOffset = 3.dp,
                        borderWidth = 2.dp
                    )
                }
            }
        }
    }
}
