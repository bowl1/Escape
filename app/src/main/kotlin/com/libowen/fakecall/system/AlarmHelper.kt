package com.libowen.fakecall.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.libowen.fakecall.domain.model.CallerInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationHelper: NotificationHelper
) {
    companion object {
        const val REQUEST_CODE = 1001
        const val EXTRA_CALLER_ID = "caller_id"
        const val EXTRA_CALLER_NAME = "caller_name"
        const val EXTRA_CALLER_NUMBER = "caller_number"
        const val EXTRA_CALLER_AVATAR = "caller_avatar"

        // 低于此阈值用 Handler，避免 AlarmManager 系统延迟
        private const val IMMEDIATE_THRESHOLD_MS = 10_000L
    }

    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val handler = Handler(Looper.getMainLooper())
    private var pendingImmediateCallback: Runnable? = null

    fun schedule(delayMs: Long, caller: CallerInfo) {
        if (delayMs < IMMEDIATE_THRESHOLD_MS) {
            // 短延迟：Handler 精确触发，不受 AlarmManager 系统调度影响
            pendingImmediateCallback?.let { handler.removeCallbacks(it) }
            val callback = Runnable { notificationHelper.showIncomingCallNotification(caller) }
            pendingImmediateCallback = callback
            handler.postDelayed(callback, delayMs)
        } else {
            // 长延迟：AlarmManager 保证后台/息屏后仍能触发
            val triggerTime = System.currentTimeMillis() + delayMs
            val pendingIntent = buildPendingIntent(caller)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun cancel() {
        // 取消 Handler 短延迟
        pendingImmediateCallback?.let {
            handler.removeCallbacks(it)
            pendingImmediateCallback = null
        }
        // 取消 AlarmManager 长延迟
        val intent = Intent(context, FakeCallReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, REQUEST_CODE, intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let { alarmManager.cancel(it) }
    }

    private fun buildPendingIntent(caller: CallerInfo): PendingIntent {
        val intent = Intent(context, FakeCallReceiver::class.java).apply {
            putExtra(EXTRA_CALLER_ID, caller.id)
            putExtra(EXTRA_CALLER_NAME, caller.name)
            putExtra(EXTRA_CALLER_NUMBER, caller.number)
            putExtra(EXTRA_CALLER_AVATAR, caller.avatarUri)
        }
        return PendingIntent.getBroadcast(
            context, REQUEST_CODE, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
