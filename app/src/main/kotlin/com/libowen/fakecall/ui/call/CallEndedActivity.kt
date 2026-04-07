package com.libowen.fakecall.ui.call

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.libowen.fakecall.system.AlarmHelper
import com.libowen.fakecall.system.NotificationHelper
import com.libowen.fakecall.ui.theme.FakeCallTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallEndedActivity : ComponentActivity() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callerName = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NAME) ?: ""
        notificationHelper.clearAll()

        setContent {
            FakeCallTheme {
                CallEndedScreen(callerName = callerName)
            }
        }

        // 2 秒后回到手机主屏
        lifecycleScope.launch {
            delay(2000)
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(homeIntent)
            finish()
        }
    }
}
