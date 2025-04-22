package dev.abhinav.alarm.alarm.presentation.ringing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhinav.alarm.alarm.domain.AlarmDataSource
import dev.abhinav.alarm.alarm.presentation.ManageAlarmUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RingingScreenViewModel @Inject constructor(
    private val localAlarmDataSource: AlarmDataSource,
    private val manageAlarmUseCase: ManageAlarmUseCase
) : ViewModel() {

    fun turnOffAlarm(alarmId: Int) {
        viewModelScope.launch {
            manageAlarmUseCase.unscheduleAlarm(alarmId)
            val alarm = localAlarmDataSource.getAlarmById(alarmId)
            if (alarm != null) {
                val updatedAlarm = alarm.copy(isEnabled = false)
                localAlarmDataSource.upsertAlarm(updatedAlarm)
            }
        }
    }
}