package com.libowen.fakecall.system

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@AndroidEntryPoint
class FakeCallService : Service() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // 通话时长（秒），暴露给 InCallViewModel
    private val _callDurationSeconds = MutableStateFlow(0)
    val callDurationSeconds: StateFlow<Int> get() = _callDurationSeconds

    // 来电者姓名（用于通知显示）
    private var callerName: String = ""

    companion object {
        const val EXTRA_CALLER_NAME = "caller_name"
        private var instance: FakeCallService? = null

        fun getInstance(): FakeCallService? = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        callerName = intent?.getStringExtra(EXTRA_CALLER_NAME) ?: ""

        val notification = notificationHelper.buildInCallNotification(
            callerName = callerName,
            durationText = "00:00"
        )
        startForeground(NotificationHelper.NOTIFICATION_IN_CALL, notification)
        startTimer()

        return START_STICKY
    }

    private fun startTimer() {
        scope.launch {
            while (isActive) {
                delay(1000)
                _callDurationSeconds.value += 1
                // 更新通知里的计时
                val duration = formatDuration(_callDurationSeconds.value)
                val updatedNotification = notificationHelper.buildInCallNotification(
                    callerName = callerName,
                    durationText = duration
                )
                val nm = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
                nm.notify(NotificationHelper.NOTIFICATION_IN_CALL, updatedNotification)
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        instance = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun formatDuration(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }
}
