package com.kyu.tabungan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.Surface

@Composable
fun NeoCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Surface,
    borderColor: Color = BorderColor,
    shadowColor: Color = HardShadowColor,
    shadowOffset: Dp = 5.dp,
    borderWidth: Dp = 2.5.dp,
    cornerRadius: Dp = 14.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier.padding(end = shadowOffset, bottom = shadowOffset)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(color = shadowColor, shape = shape)
        )

        val clickableModifier = if (onClick != null) {
            Modifier.clickable(onClick = onClick)
        } else {
            Modifier
        }

        Box(
            modifier = Modifier
                .clip(shape)
                .background(color = backgroundColor, shape = shape)
                .border(width = borderWidth, color = borderColor, shape = shape)
                .then(clickableModifier)
        ) {
            content()
        }
    }
}
