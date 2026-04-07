package com.libowen.fakecall.system

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.libowen.fakecall.R
import com.libowen.fakecall.domain.model.CallerInfo
import com.libowen.fakecall.ui.call.IncomingCallActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_INCOMING_CALL = "incoming_call"
        const val CHANNEL_IN_CALL = "in_call"
        const val NOTIFICATION_INCOMING = 100
        const val NOTIFICATION_IN_CALL = 101
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannels()
    }

    private fun createChannels() {
        // 来电通知渠道（最高优先级）
        NotificationChannel(
            CHANNEL_INCOMING_CALL,
            "Incoming Call",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Fake incoming call notifications"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }.also { notificationManager.createNotificationChannel(it) }

        // 通话中通知渠道
        NotificationChannel(
            CHANNEL_IN_CALL,
            "In Call",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Active fake call notification"
        }.also { notificationManager.createNotificationChannel(it) }
    }

    fun showIncomingCallNotification(caller: CallerInfo) {
        // Full-Screen Intent → 打开来电 Activity（锁屏也能弹出）
        val fullScreenIntent = Intent(context, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
            putExtra(AlarmHelper.EXTRA_CALLER_ID, caller.id)
            putExtra(AlarmHelper.EXTRA_CALLER_NAME, caller.name)
            putExtra(AlarmHelper.EXTRA_CALLER_NUMBER, caller.number)
            putExtra(AlarmHelper.EXTRA_CALLER_AVATAR, caller.avatarUri)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_INCOMING_CALL)
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle(caller.name)
            .setContentText(caller.number)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(false)
            .setOngoing(true)
            .build()

        notificationManager.notify(NOTIFICATION_INCOMING, notification)

        // 兜底：直接启动 Activity，防止 Full-Screen Intent 被厂商 ROM（如华为 EMUI）或虚拟空间（如 GSpace）拦截
        try {
            context.startActivity(fullScreenIntent)
        } catch (_: Exception) {
            // 忽略，通知路径已作为主路径
        }
    }

    fun buildInCallNotification(callerName: String, durationText: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_IN_CALL)
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle(callerName)
            .setContentText(durationText)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    fun clearIncomingCall() {
        notificationManager.cancel(NOTIFICATION_INCOMING)
    }

    fun clearAll() {
        notificationManager.cancelAll()
    }
}
