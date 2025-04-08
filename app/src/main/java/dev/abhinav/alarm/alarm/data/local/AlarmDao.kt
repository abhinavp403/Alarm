package dev.abhinav.alarm.alarm.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Upsert
    suspend fun upsert(alarmEntity: AlarmEntity) : Long

    @Delete
    suspend fun delete(alarmEntity: AlarmEntity)

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: Int): AlarmEntity?

    @Query("SELECT * FROM alarms ORDER BY time ASC")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteById(id: Int)
}