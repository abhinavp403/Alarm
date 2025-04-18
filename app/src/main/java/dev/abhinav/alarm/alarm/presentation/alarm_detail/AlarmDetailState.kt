package dev.abhinav.alarm.alarm.presentation.alarm_detail

import androidx.compose.runtime.Immutable
import dev.abhinav.alarm.alarm.presentation.model.AlarmUi

@Immutable
data class AlarmDetailState(
    val alarm: AlarmUi? = null,
    val hourField: String = "",
    val minuteField: String = "",
    val alarmName : String ? = null,
    val showAlarmNameDialog: Boolean = false,
) {
    val isAlarmValid: Boolean
        get() = hourField.length == 2 && minuteField.length == 2
}