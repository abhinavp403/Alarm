package dev.abhinav.alarm.alarm.presentation.alarm_detail

sealed interface AlarmDetailEvent {
    data object NavigateBack : AlarmDetailEvent
}