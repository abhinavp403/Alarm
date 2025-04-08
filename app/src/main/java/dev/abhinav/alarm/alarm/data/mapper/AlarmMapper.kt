package dev.abhinav.alarm.alarm.data.mapper

import dev.abhinav.alarm.alarm.data.local.AlarmEntity
import dev.abhinav.alarm.alarm.domain.model.Alarm

fun AlarmEntity.toDomainModel(): Alarm {
    return Alarm(
        id = this.id,
        time = this.time,
        isEnabled = this.isEnabled,
        repeatDays = this.repeatDays,
        alarmName = this.alarmName,
        ringtone = this.ringtone,
        volume = this.volume,
        vibrate = this.vibrate
    )
}

fun Alarm.toEntityModel(): AlarmEntity {
    return AlarmEntity(
        id = this.id,
        time = this.time,
        isEnabled = this.isEnabled,
        repeatDays = this.repeatDays,
        alarmName = this.alarmName,
        ringtone = this.ringtone,
        volume = this.volume,
        vibrate = this.vibrate
    )
}