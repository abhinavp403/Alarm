package dev.abhinav.alarm.alarm.presentation.alarm_list

sealed interface AlarmListEvent {
    data class NavigateToAlarmDetail(val alarmId: Int? = null) : AlarmListEvent
}