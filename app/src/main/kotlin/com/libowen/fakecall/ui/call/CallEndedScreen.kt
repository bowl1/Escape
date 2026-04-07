package com.libowen.fakecall.ui.call

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.libowen.fakecall.ui.theme.*

@Composable
fun CallEndedScreen(callerName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D1B2A), DarkBackground)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = null,
                tint = RedError,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "Call Ended",
                color = TextPrimary,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )

            if (callerName.isNotEmpty()) {
                Text(
                    text = callerName,
                    color = TextSecondary,
                    fontFamily = PlusJakartaSans,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Returning to main screen...",
                color = TextMuted,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp
            )
        }
    }
}
