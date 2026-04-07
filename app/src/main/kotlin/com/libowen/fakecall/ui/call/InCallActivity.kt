package com.libowen.fakecall.ui.call

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.system.AlarmHelper
import com.libowen.fakecall.system.FakeCallService
import com.libowen.fakecall.ui.theme.FakeCallTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InCallActivity : ComponentActivity() {

    private lateinit var caller: CallerInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        caller = CallerInfo(
            id = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_ID) ?: "unknown",
            name = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NAME) ?: "Unknown",
            number = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NUMBER) ?: "",
            avatarUri = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_AVATAR)
        )

        // 启动 Foreground Service（维护计时 + 常驻通知）
        val serviceIntent = Intent(this, FakeCallService::class.java).apply {
            putExtra(FakeCallService.EXTRA_CALLER_NAME, caller.name)
        }
        startForegroundService(serviceIntent)

        setContent {
            FakeCallTheme {
                InCallScreen(
                    caller = caller,
                    onHangUp = { handleHangUp() }
                )
            }
        }
    }

    private fun handleHangUp() {
        // 停止通话 Service
        stopService(Intent(this, FakeCallService::class.java))

        // 跳转到通话结束界面
        val intent = Intent(this, CallEndedActivity::class.java).apply {
            putExtra(AlarmHelper.EXTRA_CALLER_NAME, caller.name)
        }
        startActivity(intent)
        finish()
    }
}
