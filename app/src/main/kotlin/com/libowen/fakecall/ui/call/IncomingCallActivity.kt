package com.libowen.fakecall.ui.call

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.system.AlarmHelper
import com.libowen.fakecall.system.NotificationHelper
import com.libowen.fakecall.system.RingtoneHelper
import com.libowen.fakecall.ui.theme.FakeCallTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncomingCallActivity : ComponentActivity() {

    @Inject
    lateinit var ringtoneHelper: RingtoneHelper

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private lateinit var caller: CallerInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 亮屏 + 锁屏显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        // 从 Intent 读取来电者信息
        caller = CallerInfo(
            id = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_ID) ?: "unknown",
            name = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NAME) ?: "Unknown",
            number = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NUMBER) ?: "",
            avatarUri = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_AVATAR)
        )

        // 开始振铃
        ringtoneHelper.startRinging()

        setContent {
            FakeCallTheme {
                IncomingCallScreen(
                    caller = caller,
                    onAnswer = { handleAnswer() },
                    onDecline = { handleDecline() }
                )
            }
        }
    }

    private fun handleAnswer() {
        ringtoneHelper.stopRinging()
        notificationHelper.clearIncomingCall()

        // 启动通话中界面
        val intent = Intent(this, InCallActivity::class.java).apply {
            putExtra(AlarmHelper.EXTRA_CALLER_ID, caller.id)
            putExtra(AlarmHelper.EXTRA_CALLER_NAME, caller.name)
            putExtra(AlarmHelper.EXTRA_CALLER_NUMBER, caller.number)
            putExtra(AlarmHelper.EXTRA_CALLER_AVATAR, caller.avatarUri)
        }
        startActivity(intent)
        finish()
    }

    private fun handleDecline() {
        ringtoneHelper.stopRinging()
        notificationHelper.clearIncomingCall()
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        finish()
    }

    override fun onDestroy() {
        ringtoneHelper.stopRinging()
        super.onDestroy()
    }
}
