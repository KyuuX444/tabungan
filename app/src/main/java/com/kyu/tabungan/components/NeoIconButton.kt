package com.kyu.tabungan.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain

@Composable
fun NeoIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Surface,
    iconTint: Color = TextMain,
    borderColor: Color = BorderColor,
    shadowColor: Color = HardShadowColor,
    shadowOffset: Dp = 4.dp,
    borderWidth: Dp = 2.dp,
    cornerRadius: Dp = 10.dp,
    size: Dp = 44.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentOffset by animateDpAsState(
        targetValue = if (isPressed) shadowOffset else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "iconBtnPressAnim"
    )

    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier.padding(end = shadowOffset, bottom = shadowOffset)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = shadowOffset, y = shadowOffset)
                .background(color = shadowColor, shape = shape)
        )

        Box(
            modifier = Modifier
                .size(size)
                .offset(x = currentOffset, y = currentOffset)
                .clip(shape)
                .background(color = backgroundColor, shape = shape)
                .border(width = borderWidth, color = borderColor, shape = shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
