package com.libowen.fakecall.ui.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.libowen.fakecall.ui.theme.*

@Composable
fun IncomingCallScreen(
    caller: CallerInfo,
    onAnswer: () -> Unit,
    onDecline: () -> Unit
) {
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
            Spacer(Modifier.weight(0.15f))

            // 来电者信息
            Text(
                text = "Incoming Call",
                color = TextSecondary,
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(32.dp))

            CallerAvatar(avatarUri = caller.avatarUri, size = 120.dp)

            Spacer(Modifier.height(20.dp))

            Text(
                text = caller.name,
                color = TextPrimary,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = caller.number,
                color = TextSecondary,
                fontFamily = PlusJakartaSans,
                fontSize = 18.sp
            )

            Spacer(Modifier.weight(1f))

            // 接听 / 拒接 按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 64.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 拒接
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CallActionButton(
                        icon = Icons.Default.CallEnd,
                        backgroundColor = RedError,
                        size = 72.dp,
                        iconSize = 32.dp,
                        onClick = onDecline
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Decline",
                        color = TextSecondary,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp
                    )
                }

                // 接听
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CallActionButton(
                        icon = Icons.Default.Call,
                        backgroundColor = GreenAccent,
                        size = 72.dp,
                        iconSize = 32.dp,
                        onClick = onAnswer
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Answer",
                        color = TextSecondary,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
