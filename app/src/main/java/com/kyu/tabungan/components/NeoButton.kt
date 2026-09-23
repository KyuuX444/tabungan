package com.kyu.tabungan.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.Surface

@Composable
fun NeoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BrightBlue,
    contentColor: Color = Surface,
    borderColor: Color = BorderColor,
    shadowColor: Color = HardShadowColor,
    shadowOffset: Dp = 5.dp,
    borderWidth: Dp = 2.5.dp,
    cornerRadius: Dp = 12.dp,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) shadowOffset else 0.dp,
        animationSpec = tween(durationMillis = 80),
        label = "btnPressAnim"
    )

    val shape = RoundedCornerShape(cornerRadius)
    val actualBg = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f)

    Box(
        modifier = modifier.padding(end = shadowOffset, bottom = shadowOffset),
        propagateMinConstraints = true
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(color = if (enabled) shadowColor else shadowColor.copy(alpha = 0.3f), shape = shape)
        )

        Box(
            modifier = Modifier
                .offset(x = currentOffset, y = currentOffset)
                .clip(shape)
                .background(color = actualBg, shape = shape)
                .border(width = borderWidth, color = borderColor, shape = shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    androidx.compose.material3.Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = text,
                    color = contentColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
