package com.libowen.fakecall.ui.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.ui.components.CallerAvatar
import com.libowen.fakecall.ui.theme.*

@Composable
fun ContactsScreen(
    onBack: () -> Unit,
    onContactSelected: (CallerInfo) -> Unit,
    onNavigateToAddContact: () -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ──────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable(onClick = onBack),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Back",
                        color = TextPrimary,
                        fontFamily = PlusJakartaSans,
                        fontSize = 15.sp
                    )
                }

                Text(
                    text = "My Contacts",
                    color = TextPrimary,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                // Add Contact 按钮
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add Contact",
                    tint = PurpleLight,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onNavigateToAddContact)
                )
            }

            // ── Contact List ─────────────────────────────────────────
            if (contacts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonOff,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "No contacts yet",
                            color = TextMuted,
                            fontFamily = PlusJakartaSans,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Tap + to add a contact",
                            color = TextDisabled,
                            fontFamily = PlusJakartaSans,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(contacts, key = { it.id }) { caller ->
                        ContactRow(
                            caller = caller,
                            onChoose = {
                                onContactSelected(caller)
                            },
                            onDelete = { viewModel.delete(caller) }
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ContactRow(
    caller: CallerInfo,
    onChoose: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkCard)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        CallerAvatar(avatarUri = caller.avatarUri, size = 44.dp)

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = caller.name,
                    color = TextPrimary,
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
            Text(
                text = caller.number,
                color = TextSecondary,
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp
            )
        }

        // Choose 按钮
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1A0F3A))
                .clickable(onClick = onChoose)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = "Choose",
                color = PurpleLight,
                fontFamily = PlusJakartaSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }

        // Delete 按钮
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = RedError,
            modifier = Modifier
                .size(18.dp)
                .clickable(onClick = onDelete)
        )
    }
}

