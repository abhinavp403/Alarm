package dev.abhinav.alarm.alarm.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.abhinav.alarm.alarm.data.local.AlarmDatabase
import dev.abhinav.alarm.alarm.data.local.LocalAlarmDataSource
import dev.abhinav.alarm.alarm.domain.AlarmDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindAlarmDataSource(localAlarmDataSource: LocalAlarmDataSource): AlarmDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AlarmDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AlarmDatabase::class.java,
            "alarm_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlarmDao(alarmDatabase: AlarmDatabase) = alarmDatabase.alarmDao()
}