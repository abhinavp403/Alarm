package dev.abhinav.alarm.alarm.presentation.alarm_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhinav.alarm.alarm.domain.AlarmDataSource
import dev.abhinav.alarm.alarm.presentation.ManageAlarmUseCase
import dev.abhinav.alarm.alarm.presentation.alarm_detail.util.AlarmUtils
import dev.abhinav.alarm.alarm.presentation.model.AlarmUi
import dev.abhinav.alarm.alarm.presentation.model.toAlarm
import dev.abhinav.alarm.alarm.presentation.model.toAlarmUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlarmDetailViewModel @Inject constructor(
    private val localAlarmDataSource: AlarmDataSource,
    private val manageAlarmUseCase: ManageAlarmUseCase,
) : ViewModel() {

    private val _alarmDetailEvents = Channel<AlarmDetailEvent>()
    val alarmDetailEvents = _alarmDetailEvents.receiveAsFlow()


    private val _uiState = MutableStateFlow(AlarmDetailState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        initialValue = AlarmDetailState(),
        started = SharingStarted.WhileSubscribed(5_000)
    )


    fun loadAlarm(alarmId: Int?) {
        viewModelScope.launch {
            val alarm = alarmId?.let { localAlarmDataSource.getAlarmById(it) }?.toAlarmUi()
            _uiState.update {
                it.copy(
                    alarm = alarm,
                    hourField = alarm?.dateTime?.hour?.toString()?.padStart(2,'0') ?: "",
                    minuteField = alarm?.dateTime?.minute?.toString()?.padStart(2,'0') ?: "",
                    alarmName = alarm?.title,
                )
            }
        }
    }

    fun onTimeChange(hour: String? = null, minute: String? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    hourField = hour ?: it.hourField,
                    minuteField = minute ?: it.minuteField
                )
            }
        }
    }

    fun onAlarmNameChange(alarmName: String) {
        _uiState.update {
            it.copy(
                alarmName = alarmName,
                showAlarmNameDialog = false
            )
        }
    }


    fun saveAlarm() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.isAlarmValid) {
                val hour = currentState.hourField.toIntOrNull() ?: 0
                val minute = currentState.minuteField.toIntOrNull() ?: 0
                val dateTime = LocalDateTime.now()
                    .withHour(hour)
                    .withMinute(minute)
                    .withSecond(0)
                    .withNano(0)

                val alarm = currentState.alarm?.copy(
                    title = if (currentState.alarmName?.isNotBlank() == true) currentState.alarmName else null,
                    dateTime = dateTime
                ) ?: AlarmUi(
                    title = if (currentState.alarmName?.isNotBlank() == true) currentState.alarmName else null,
                    dateTime = dateTime,
                    isEnabled = true,
                    repeatDays = emptyList()
                )
                val addedAlarmId = localAlarmDataSource.upsertAlarm(alarm.toAlarm())
                val triggerTime = AlarmUtils.getTriggerTime(alarm.dateTime.hour, alarm.dateTime.minute)
                manageAlarmUseCase.scheduleAlarm(addedAlarmId.toInt(), triggerTime)
                _alarmDetailEvents.send(AlarmDetailEvent.NavigateBack)
            }
        }
    }


    fun showAlarmDialog(shouldShow: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(showAlarmNameDialog = shouldShow)
            }
        }
    }

    fun onAction(action: AlarmDetailAction) {
        viewModelScope.launch {
            when (action) {
                is AlarmDetailAction.OnAlarmNameClick -> {
                    showAlarmDialog(true)
                }

                AlarmDetailAction.OnBackClick -> {
                    _alarmDetailEvents.send(AlarmDetailEvent.NavigateBack)
                }

                AlarmDetailAction.OnDismissAlarmNameDialog -> {
                    showAlarmDialog(false)
                }

                is AlarmDetailAction.OnSaveAlarmName -> {
                    onAlarmNameChange(action.alarmName ?: "")
                }

                AlarmDetailAction.OnSaveClick -> {
                    saveAlarm()
                }

                is AlarmDetailAction.OnTimeChange -> {
                    onTimeChange(action.hour, action.minute)
                }
                is AlarmDetailAction.OnLoadAlarm -> {
                    loadAlarm(action.alarmId)
                }
            }
        }
    }
}