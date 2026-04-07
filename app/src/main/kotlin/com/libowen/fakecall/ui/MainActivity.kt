package com.libowen.fakecall.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.libowen.fakecall.ui.contacts.AddContactScreen
import com.libowen.fakecall.ui.contacts.ContactsScreen
import com.libowen.fakecall.ui.home.HomeScreen
import com.libowen.fakecall.ui.home.HomeViewModel
import com.libowen.fakecall.ui.theme.FakeCallTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 请求通知权限（Android 13+）
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* 权限结果处理，暂时无需额外处理 */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 请求 POST_NOTIFICATIONS 权限（Android 13+），仅在未授权时请求一次
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            FakeCallTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // 共享 HomeViewModel，使得 Contacts 选中后能直接更新 Home 状态
                    val homeViewModel: HomeViewModel = hiltViewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModel = homeViewModel,
                                onNavigateToContacts = {
                                    navController.navigate("contacts")
                                }
                            )
                        }
                        composable("contacts") {
                            ContactsScreen(
                                onBack = { navController.popBackStack() },
                                onContactSelected = { caller ->
                                    homeViewModel.selectContact(caller)
                                    navController.popBackStack()
                                },
                                onNavigateToAddContact = {
                                    navController.navigate("add_contact")
                                }
                            )
                        }
                        composable("add_contact") {
                            AddContactScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
