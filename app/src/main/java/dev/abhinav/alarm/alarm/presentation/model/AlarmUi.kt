package dev.abhinav.alarm.alarm.presentation.model

import dev.abhinav.alarm.alarm.domain.model.Alarm
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class AlarmUi(
    val id: Int? = null,
    val title: String? = null,
    val dateTime: LocalDateTime,
    val isEnabled: Boolean = false,
    val remainingTime: String? = null,
    val repeatDays: List<DayOfWeek>
) {
    val time: String
        get() = dateTime.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))
}

fun Alarm.toAlarmUi(): AlarmUi {
    val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
    val remainingTime = calculateRemainingTime(localDateTime.toLocalTime())

    return AlarmUi(
        id = id,
        title = alarmName ?: "Alarm",
        dateTime = localDateTime,
        isEnabled = isEnabled,
        remainingTime = remainingTime,
        repeatDays = repeatDays.map { DayOfWeek.valueOf(it) }
    )
}

fun AlarmUi.toAlarm(): Alarm {
    return Alarm(
        id = id,
        alarmName = title,
        time = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
        repeatDays = repeatDays.map { it.name },
        isEnabled = isEnabled
    )
}

fun calculateRemainingTime(alarmTime: LocalTime): String {
    val now = LocalDateTime.now()
    val alarmDateTime =
        if (alarmTime.isAfter(now.toLocalTime()) || alarmTime == now.toLocalTime()) {
            now.withHour(alarmTime.hour).withMinute(alarmTime.minute).withSecond(0).withNano(0)
        } else {
            now.plusDays(1).withHour(alarmTime.hour).withMinute(alarmTime.minute).withSecond(0)
                .withNano(0)
        }

    val duration = java.time.Duration.between(now, alarmDateTime)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}min"
        else -> "${minutes}min"
    }
}

enum class DayOfWeek {
    Mo, Tu, We, Th, Fr, Sa, Su
}