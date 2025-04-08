package dev.abhinav.alarm.alarm.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val time: String, // Format "HH:mm"
    val isEnabled: Boolean,
    val repeatDays: List<String>,
    val alarmName: String? = null,
    val ringtone: String? = null,
    val volume: Int = 50,
    val vibrate: Boolean = true
)