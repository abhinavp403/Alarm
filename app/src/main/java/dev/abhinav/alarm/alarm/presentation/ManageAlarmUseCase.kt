package dev.abhinav.alarm.alarm.presentation

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Calendar
import javax.inject.Inject

class ManageAlarmUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleAlarm(alarmId: Int, triggerTime: Calendar) {
        AlarmScheduler.scheduleAlarm(context, alarmId, triggerTime)
    }

    fun unscheduleAlarm(alarmId: Int) {
        AlarmScheduler.cancelAlarm(context, alarmId)
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ManageAlarmUseCaseEntryPoint {
    fun manageAlarmUseCase(): ManageAlarmUseCase
}