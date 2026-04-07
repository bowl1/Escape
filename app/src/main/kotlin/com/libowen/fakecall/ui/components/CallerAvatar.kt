package com.libowen.fakecall.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.libowen.fakecall.ui.theme.CyanAccent
import com.libowen.fakecall.ui.theme.DarkCardSecondary
import com.libowen.fakecall.ui.theme.TextSecondary

@Composable
fun CallerAvatar(
    avatarUri: String?,
    size: Dp = 80.dp,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(DarkCardSecondary)
            .border(2.dp, CyanAccent, CircleShape)
    ) {
        if (!avatarUri.isNullOrEmpty()) {
            AsyncImage(
                model = avatarUri,
                contentDescription = "Caller avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default avatar",
                tint = TextSecondary,
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}
