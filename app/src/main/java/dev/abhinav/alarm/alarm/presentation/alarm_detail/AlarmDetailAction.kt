package dev.abhinav.alarm.alarm.presentation.alarm_detail

sealed interface AlarmDetailAction {
    data class OnLoadAlarm(val alarmId: Int? = null) : AlarmDetailAction
    data class OnSaveAlarmName(val alarmName: String? = null) : AlarmDetailAction
    data object OnSaveClick : AlarmDetailAction
    data object OnBackClick : AlarmDetailAction
    data object OnAlarmNameClick : AlarmDetailAction
    data class OnTimeChange(val hour: String, val minute: String) : AlarmDetailAction
    data object OnDismissAlarmNameDialog : AlarmDetailAction
}
