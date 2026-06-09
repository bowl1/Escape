package com.libowen.fakecall.ui.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.ui.components.CallerAvatar
import com.libowen.fakecall.ui.components.CallActionButton
import com.libowen.fakecall.ui.components.TimerText
import com.libowen.fakecall.ui.theme.*
import kotlinx.coroutines.delay
import androidx.compose.runtime.produceState

@Composable
fun InCallScreen(
    caller: CallerInfo,
    onHangUp: () -> Unit
) {
    val serviceSeconds by produceState(0) {
        while (true) {
            delay(1000)
            value += 1
        }
    }

    // 本地 mute / speaker 状态（仅 UI 切换）
    var isMuted by remember { mutableStateOf(false) }
    var isSpeaker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D1B2A), DarkBackground)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.1f))

            Text(
                text = "On Call",
                color = GreenAccent,
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(24.dp))

            CallerAvatar(avatarUri = caller.avatarUri, size = 100.dp)

            Spacer(Modifier.height(16.dp))

            Text(
                text = caller.name,
                color = TextPrimary,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )

            Spacer(Modifier.height(8.dp))

            TimerText(seconds = serviceSeconds, fontSize = 20.sp)

            Spacer(Modifier.weight(1f))

            // 辅助按钮行（静音 / 扬声器 / 键盘）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 静音
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CallActionButton(
                        icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        backgroundColor = if (isMuted) DarkCardSecondary else DarkCard,
                        size = 56.dp,
                        iconSize = 24.dp,
                        onClick = { isMuted = !isMuted }
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (isMuted) "Unmute" else "Mute",
                        color = TextSecondary,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp
                    )
                }

                // 扬声器
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CallActionButton(
                        icon = if (isSpeaker) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        backgroundColor = if (isSpeaker) DarkCardSecondary else DarkCard,
                        size = 56.dp,
                        iconSize = 24.dp,
                        onClick = { isSpeaker = !isSpeaker }
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Speaker",
                        color = TextSecondary,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp
                    )
                }

                // 拨号键盘（UI only）
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CallActionButton(
                        icon = Icons.Default.Dialpad,
                        backgroundColor = DarkCard,
                        size = 56.dp,
                        iconSize = 24.dp,
                        onClick = { /* TODO: 展开键盘 */ }
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Keypad",
                        color = TextSecondary,
                        fontFamily = PlusJakartaSans,
                        fontSize = 12.sp
                    )
                }
            }

            // 挂断按钮
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 64.dp)
            ) {
                CallActionButton(
                    icon = Icons.Default.CallEnd,
                    backgroundColor = RedError,
                    size = 72.dp,
                    iconSize = 32.dp,
                    onClick = onHangUp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "End Call",
                    color = TextSecondary,
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp
                )
            }
        }
    }
}
