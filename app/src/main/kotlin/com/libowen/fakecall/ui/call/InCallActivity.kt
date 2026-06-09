package com.libowen.fakecall.ui.call

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.system.AlarmHelper
import com.libowen.fakecall.system.NotificationHelper
import com.libowen.fakecall.ui.theme.FakeCallTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InCallActivity : ComponentActivity() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private lateinit var caller: CallerInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        caller = CallerInfo(
            id = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_ID) ?: "unknown",
            name = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NAME) ?: "Unknown",
            number = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NUMBER) ?: "",
            avatarUri = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_AVATAR)
        )

        notificationHelper.showInCallNotification(caller.name)

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
        notificationHelper.clearInCallNotification()
        val intent = Intent(this, CallEndedActivity::class.java).apply {
            putExtra(AlarmHelper.EXTRA_CALLER_NAME, caller.name)
        }
        startActivity(intent)
        finish()
    }
}
