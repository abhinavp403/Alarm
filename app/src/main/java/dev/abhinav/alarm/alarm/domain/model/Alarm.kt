package dev.abhinav.alarm.alarm.domain.model

data class Alarm(
    val id: Int? = null,
    val time: String, // "HH:mm"
    val isEnabled: Boolean,
    val repeatDays: List<String>,
    val alarmName: String? = null,
    val ringtone: String? = null,
    val volume: Int = 50,
    val vibrate: Boolean = true
)