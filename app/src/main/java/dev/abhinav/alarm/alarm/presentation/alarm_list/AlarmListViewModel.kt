package dev.abhinav.alarm.alarm.presentation.alarm_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abhinav.alarm.alarm.domain.AlarmDataSource
import dev.abhinav.alarm.alarm.presentation.ManageAlarmUseCase
import dev.abhinav.alarm.alarm.presentation.alarm_detail.util.AlarmUtils
import dev.abhinav.alarm.alarm.presentation.model.toAlarm
import dev.abhinav.alarm.alarm.presentation.model.toAlarmUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmListViewModel @Inject constructor(
    private val localAlarmDataSource: AlarmDataSource,
    private val manageAlarmUseCase: ManageAlarmUseCase
) : ViewModel() {

    private val _alarmListEvents = Channel<AlarmListEvent>()
    val alarmListEvents = _alarmListEvents.receiveAsFlow()

    private val _alarmListState = MutableStateFlow(AlarmListState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = _alarmListState
        .flatMapLatest {
            localAlarmDataSource.getAllAlarms()
                .map { alarms ->
                    AlarmListState(uiState = AlarmListUIState.Success(alarms.map { it.toAlarmUi() }))
                }
                .catch { e ->
                    emit(
                        AlarmListState(
                            uiState = AlarmListUIState.Error(e.message ?: "Unknown error")
                        )
                    )
                }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AlarmListState(AlarmListUIState.Loading)
        )

    fun onAlarmSwitchClick(alarmId: Int, isChecked: Boolean) {
        viewModelScope.launch {
            val currentState = state.value.uiState
            if (currentState is AlarmListUIState.Success) {
                val updatedAlarm = currentState.alarms.find { it.id == alarmId }?.copy(isEnabled = isChecked)
                if (updatedAlarm != null) {
                    localAlarmDataSource.upsertAlarm(updatedAlarm.toAlarm())
                    if (isChecked) {
                        val triggerTime = AlarmUtils.getTriggerTime(updatedAlarm.dateTime.hour, updatedAlarm.dateTime.minute)
                        manageAlarmUseCase.scheduleAlarm(alarmId, triggerTime)
                    } else {
                        manageAlarmUseCase.unscheduleAlarm(alarmId)
                    }
                }
            }
        }
    }


    fun onAction(action: AlarmListAction) {
        viewModelScope.launch {
            when (action) {
                is AlarmListAction.OnAlarmSwitchClick -> {
                    onAlarmSwitchClick(action.alarmId, action.isChecked)
                }

                AlarmListAction.OnAddNewAlarmClick -> {
                    _alarmListEvents.send(AlarmListEvent.NavigateToAlarmDetail())
                }

                is AlarmListAction.OnAlarmClick -> {
                    _alarmListEvents.send(AlarmListEvent.NavigateToAlarmDetail(action.alarmId))
                }
            }
        }
    }
}