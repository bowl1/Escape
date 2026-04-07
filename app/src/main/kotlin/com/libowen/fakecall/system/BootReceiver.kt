package com.libowen.fakecall.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 接收开机广播，预留用于恢复定时任务。
 * 当前版本暂时空实现，未来可在此恢复 AlarmManager 任务。
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // TODO: 恢复已保存的定时任务（如果用户设置过定时来电）
        }
    }
}
