package com.libowen.fakecall.ui.contacts

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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun AddContactScreen(
    onBack: () -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<String?>(null) }
    var setDefault by remember { mutableStateOf(false) }
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
            avatarUri = it.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(com.libowen.fakecall.ui.theme.DarkBackground)
            .imePadding()
    ) {
        // ── Top Bar ──────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
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
                    tint = com.libowen.fakecall.ui.theme.TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Contacts",
                    color = com.libowen.fakecall.ui.theme.TextPrimary,
                    fontFamily = com.libowen.fakecall.ui.theme.PlusJakartaSans,
                    fontSize = 15.sp
                )
            }

            Text(
                text = "New Contact",
                color = com.libowen.fakecall.ui.theme.TextPrimary,
                fontFamily = com.libowen.fakecall.ui.theme.PlusJakartaSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Button(
                onClick = {
                    viewModel.save(name, number, avatarUri, setDefault)
                    onBack()
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = com.libowen.fakecall.ui.theme.PurplePrimary,
                    disabledContainerColor = com.libowen.fakecall.ui.theme.PurplePrimary.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Save",
                    fontFamily = com.libowen.fakecall.ui.theme.PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        // ── Form ─────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            // Avatar section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(com.libowen.fakecall.ui.theme.DarkCardSecondary)
                        .border(2.dp, Color(0xFF334155), CircleShape)
                        .clickable { imagePicker.launch("image/*") }
                ) {
                    if (avatarUri != null) {
                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = com.libowen.fakecall.ui.theme.TextDisabled,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Text(
                    text = "Add Photo (optional)",
                    color = com.libowen.fakecall.ui.theme.PurpleLight,
                    fontFamily = com.libowen.fakecall.ui.theme.PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { imagePicker.launch("image/*") }
                )
            }

            // Name field
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Name",
                    color = com.libowen.fakecall.ui.theme.TextSecondary,
                    fontFamily = com.libowen.fakecall.ui.theme.PlusJakartaSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = {
                        Text(
                            "e.g. Mor, Chef, Anders...",
                            color = com.libowen.fakecall.ui.theme.TextDisabled
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = com.libowen.fakecall.ui.theme.TextPrimary,
                        unfocusedTextColor = com.libowen.fakecall.ui.theme.TextPrimary,
                        focusedContainerColor = com.libowen.fakecall.ui.theme.DarkSurface,
                        unfocusedContainerColor = com.libowen.fakecall.ui.theme.DarkSurface,
                        focusedBorderColor = com.libowen.fakecall.ui.theme.PurplePrimary,
                        unfocusedBorderColor = com.libowen.fakecall.ui.theme.DarkCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Phone Number field
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Phone Number",
                    color = com.libowen.fakecall.ui.theme.TextSecondary,
                    fontFamily = com.libowen.fakecall.ui.theme.PlusJakartaSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    placeholder = {
                        Text(
                            "e.g. 22 45 81 36",
                            color = com.libowen.fakecall.ui.theme.TextDisabled
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = com.libowen.fakecall.ui.theme.TextPrimary,
                        unfocusedTextColor = com.libowen.fakecall.ui.theme.TextPrimary,
                        focusedContainerColor = com.libowen.fakecall.ui.theme.DarkSurface,
                        unfocusedContainerColor = com.libowen.fakecall.ui.theme.DarkSurface,
                        focusedBorderColor = com.libowen.fakecall.ui.theme.PurplePrimary,
                        unfocusedBorderColor = com.libowen.fakecall.ui.theme.DarkCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Set as Default toggle row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(com.libowen.fakecall.ui.theme.DarkSurface)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Set as Default",
                    color = com.libowen.fakecall.ui.theme.TextPrimary,
                    fontFamily = com.libowen.fakecall.ui.theme.PlusJakartaSans,
                    fontSize = 15.sp
                )
                Switch(
                    checked = setDefault,
                    onCheckedChange = { setDefault = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = com.libowen.fakecall.ui.theme.PurplePrimary,
                        uncheckedThumbColor = com.libowen.fakecall.ui.theme.TextDisabled,
                        uncheckedTrackColor = com.libowen.fakecall.ui.theme.DarkCardSecondary
                    )
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
