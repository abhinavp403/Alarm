package dev.abhinav.alarm.alarm.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("alarmId", -1)
        if (alarmId != -1) {
            NotificationHelper.showNotification(context, alarmId)
        }
    }
}