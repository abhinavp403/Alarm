package dev.abhinav.alarm.alarm.presentation.alarm_list

sealed interface AlarmListAction {
    data object OnAddNewAlarmClick : AlarmListAction
    data class OnAlarmClick(val alarmId: Int) : AlarmListAction
    data class OnAlarmSwitchClick(val alarmId: Int, val isChecked: Boolean) : AlarmListAction
}