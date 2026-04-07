package com.libowen.fakecall.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.libowen.fakecall.ui.theme.PlusJakartaSans
import com.libowen.fakecall.ui.theme.TextSecondary

@Composable
fun TimerText(
    seconds: Int,
    modifier: Modifier = Modifier,
    color: Color = TextSecondary,
    fontSize: TextUnit = 20.sp
) {
    val m = seconds / 60
    val s = seconds % 60
    Text(
        text = "%02d:%02d".format(m, s),
        color = color,
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = fontSize,
        modifier = modifier
    )
}
