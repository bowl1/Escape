package com.libowen.fakecall.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.libowen.fakecall.data.datastore.SettingsDataStore
import com.libowen.fakecall.domain.model.CallerInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FakeCallReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onReceive(context: Context, intent: Intent) {
        val caller = CallerInfo(
            id = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_ID) ?: "unknown",
            name = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NAME) ?: "Unknown",
            number = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_NUMBER) ?: "",
            avatarUri = intent.getStringExtra(AlarmHelper.EXTRA_CALLER_AVATAR)
        )
        notificationHelper.showIncomingCallNotification(caller)

        // Alarm 已触发，重置 scheduled 状态
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                settingsDataStore.setScheduled(false)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
