package com.libowen.fakecall.ui.home

import androidx.compose.animation.animateColorAsState
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneCallback
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.libowen.fakecall.ui.components.CallerAvatar
import com.libowen.fakecall.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    onNavigateToContacts: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showScheduleDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ──────────────────────────────────────────────
            HeaderSection()

            // ── Tab Switcher (fixed, not scrollable) ─────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                TabSwitcher(
                    activeTab = state.activeTab,
                    onTabSelected = viewModel::setTab
                )
            }

            // ── Tab Content ─────────────────────────────────────────
            when (state.activeTab) {
                CallTab.RANDOM -> RandomTabContent(
                    scheduleState = state.scheduleState,
                    delayMs = state.delayMs,
                    onCallNow = viewModel::callNow,
                    onDelayChanged = viewModel::setDelayMs,
                    onSchedule = {
                        if (state.scheduleState == ScheduleState.IDLE) {
                            showScheduleDialog = true
                        } else {
                            viewModel.cancelScheduledCall()
                        }
                    }
                )
                CallTab.CUSTOM -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomTabContent(
                        name = state.customName,
                        number = state.customNumber,
                        avatarUri = state.customAvatarUri,
                        scheduleState = state.scheduleState,
                        delayMs = state.delayMs,
                        onNameChange = viewModel::setCustomName,
                        onNumberChange = viewModel::setCustomNumber,
                        onAvatarSelected = viewModel::setCustomAvatarUri,
                        onNavigateToContacts = onNavigateToContacts,
                        onCallNow = viewModel::callNow,
                        onDelayChanged = viewModel::setDelayMs,
                        onSchedule = {
                            if (state.scheduleState == ScheduleState.IDLE) {
                                showScheduleDialog = true
                            } else {
                                viewModel.cancelScheduledCall()
                            }
                        }
                    )
                }
            }
        }
    }

    // ── Schedule 确认弹窗 ────────────────────────────────────────────
    if (showScheduleDialog) {
        ScheduleConfirmDialog(
            delayMs = state.delayMs,
            onConfirm = {
                viewModel.scheduleCall()
                showScheduleDialog = false
            },
            onDismiss = { showScheduleDialog = false }
        )
    }
}

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(DarkCardSecondary, DarkSurface)
                )
            )
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column {
            Text(
                text = "Escape",
                color = TextPrimary,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Fake call gracefully anytime",
                color = TextPrimary.copy(alpha = 0.9f),
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun TabSwitcher(
    activeTab: CallTab,
    onTabSelected: (CallTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkCard)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CallTab.entries.forEach { tab ->
            val isActive = tab == activeTab
            val bg by animateColorAsState(
                targetValue = if (isActive) PurplePrimary else Color.Transparent,
                label = "tabBg"
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bg)
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (tab == CallTab.RANDOM) Icons.Default.Casino else Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isActive) TextPrimary else TextMuted,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (tab == CallTab.RANDOM) "Random" else "Custom",
                    color = if (isActive) TextPrimary else TextMuted,
                    fontFamily = PlusJakartaSans,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun RandomTabContent(
    scheduleState: ScheduleState,
    delayMs: Long,
    onCallNow: () -> Unit,
    onDelayChanged: (Long) -> Unit,
    onSchedule: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tap to generate a random caller and call immediately",
            color = TextSecondary,
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhoneCallback,
                contentDescription = null,
                tint = GreenAccent,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Call Now",
                color = GreenAccent,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(Modifier.height(8.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(listOf(GreenAccent, Color(0xFF16A34A)))
                )
                .clickable(onClick = onCallNow)
        ) {
            Icon(
                imageVector = Icons.Default.PhoneCallback,
                contentDescription = "Call Now",
                tint = Color.White,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Tap to call immediately",
            color = TextMuted,
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp
        )

        Spacer(Modifier.weight(1f))

        ScheduledSection(
            scheduleState = scheduleState,
            delayMs = delayMs,
            onDelayChanged = onDelayChanged,
            onSchedule = onSchedule
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CustomTabContent(
    name: String,
    number: String,
    avatarUri: String?,
    scheduleState: ScheduleState,
    delayMs: Long,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onAvatarSelected: (String?) -> Unit,
    onNavigateToContacts: () -> Unit,
    onCallNow: () -> Unit,
    onDelayChanged: (Long) -> Unit,
    onSchedule: () -> Unit
) {
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            onAvatarSelected(it.toString())
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Contact section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contact",
                    color = TextPrimary,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                // My Contacts 链接
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onNavigateToContacts)
                ) {
                    Text(
                        text = "My Contacts",
                        color = PurpleLight,
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PurpleLight,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(14.dp)
                    )
                }
            }

            // Name 输入框
            Text(
                text = "Name",
                color = TextSecondary,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = {
                    Text(
                        "e.g. Mom, Boss, James...",
                        color = TextDisabled,
                        fontSize = 15.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkCardSecondary,
                    unfocusedContainerColor = DarkCardSecondary,
                    focusedBorderColor = PurplePrimary,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Phone 输入框
            Text(
                text = "Phone Number",
                color = TextSecondary,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            OutlinedTextField(
                value = number,
                onValueChange = onNumberChange,
                placeholder = {
                    Text(
                        "e.g. 22 45 81 36",
                        color = TextDisabled,
                        fontSize = 15.sp
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkCardSecondary,
                    unfocusedContainerColor = DarkCardSecondary,
                    focusedBorderColor = PurplePrimary,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Avatar row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Avatar (optional)",
                    color = TextDisabled,
                    fontFamily = PlusJakartaSans,
                    fontSize = 13.sp
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBackground)
                        .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                        .clickable { imagePicker.launch("image/*") }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (avatarUri != null) {
                        CallerAvatar(avatarUri = avatarUri, size = 20.dp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = if (avatarUri != null) "Change Avatar" else "Choose Avatar",
                        color = TextSecondary,
                        fontFamily = PlusJakartaSans,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Call Now 标签 + 圆形按钮
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhoneCallback,
                contentDescription = null,
                tint = GreenAccent,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Call Now",
                color = GreenAccent,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(GreenAccent)
                .clickable(onClick = onCallNow)
        ) {
            Icon(
                imageVector = Icons.Default.PhoneCallback,
                contentDescription = "Custom Call Now",
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
        }

        Text(
            text = "Tap to call immediately",
            color = TextMuted,
            fontFamily = PlusJakartaSans,
            fontSize = 13.sp
        )

        // Scheduled Trigger
        ScheduledSection(
            scheduleState = scheduleState,
            delayMs = delayMs,
            onDelayChanged = onDelayChanged,
            onSchedule = onSchedule
        )
    }
}

@Composable
private fun ScheduledSection(
    scheduleState: ScheduleState,
    delayMs: Long,
    onDelayChanged: (Long) -> Unit,
    onSchedule: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkCardSecondary)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = CyanAccent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Scheduled Trigger",
                color = TextPrimary,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        // Delay slider: give 1-60s more track space, then compress 2-8 min.
        val sliderValue = delayMsToSliderValue(delayMs)
        val delayLabel = formatDelay(delayMs)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Delay Time",
                color = TextPrimary,
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = delayLabel,
                color = CyanAccent,
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Slider(
            value = sliderValue,
            onValueChange = { onDelayChanged(sliderValueToDelayMs(it)) },
            colors = SliderDefaults.colors(
                thumbColor = CyanAccent,
                activeTrackColor = CyanAccent,
                inactiveTrackColor = TextDisabled
            )
        )

        // Schedule / Cancel 按钮
        val isScheduled = scheduleState == ScheduleState.SCHEDULED
        val btnColor by animateColorAsState(
            targetValue = if (isScheduled) OrangeAccent else CyanAccent,
            label = "scheduleBtn"
        )

        Button(
            onClick = onSchedule,
            colors = ButtonDefaults.buttonColors(containerColor = btnColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = DarkBackground,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (isScheduled) "Call in ${formatDelay(delayMs)} · Tap to cancel"
                       else "Set Scheduled Call",
                color = DarkBackground,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun ScheduleConfirmDialog(
    delayMs: Long,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(DarkCardSecondary)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Schedule Confirmed",
                color = TextPrimary,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Your fake call will arrive in ${formatDelay(delayMs)}.",
                color = TextSecondary,
                fontFamily = PlusJakartaSans,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = TextSecondary)
                }
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Confirm", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

private const val SecondsSliderWeight = 0.62f
private const val MinDelaySeconds = 1
private const val MaxSecondsDelay = 60
private const val MinMinuteDelay = 2
private const val MaxMinuteDelay = 8

private fun delayMsToSliderValue(ms: Long): Float {
    val totalSec = (ms / 1000).coerceIn(MinDelaySeconds.toLong(), (MaxMinuteDelay * 60).toLong())
    return if (totalSec <= MaxSecondsDelay) {
        val secondsProgress = (totalSec - MinDelaySeconds).toFloat() / (MaxSecondsDelay - MinDelaySeconds)
        secondsProgress * SecondsSliderWeight
    } else {
        val minutes = (totalSec / 60).coerceIn(MinMinuteDelay.toLong(), MaxMinuteDelay.toLong())
        val minutesProgress = (minutes - MinMinuteDelay).toFloat() / (MaxMinuteDelay - MinMinuteDelay)
        SecondsSliderWeight + (minutesProgress * (1f - SecondsSliderWeight))
    }.coerceIn(0f, 1f)
}

private fun sliderValueToDelayMs(value: Float): Long {
    val slider = value.coerceIn(0f, 1f)
    return if (slider <= SecondsSliderWeight) {
        val secondsProgress = slider / SecondsSliderWeight
        val seconds = MinDelaySeconds + (secondsProgress * (MaxSecondsDelay - MinDelaySeconds)).roundToInt()
        seconds * 1000L
    } else {
        val minutesProgress = (slider - SecondsSliderWeight) / (1f - SecondsSliderWeight)
        val minutes = MinMinuteDelay + (minutesProgress * (MaxMinuteDelay - MinMinuteDelay)).roundToInt()
        minutes * 60 * 1000L
    }
}

private fun formatDelay(ms: Long): String {
    val totalSec = ms / 1000
    return when {
        totalSec <= 60 -> "${totalSec}s"
        totalSec < 3600 -> "${totalSec / 60} min"
        else -> {
            val h = totalSec / 3600
            val m = (totalSec % 3600) / 60
            if (m == 0L) "${h}h" else "${h}h ${m}min"
        }
    }
}
