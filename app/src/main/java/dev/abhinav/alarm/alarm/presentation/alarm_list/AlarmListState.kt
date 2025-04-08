package dev.abhinav.alarm.alarm.presentation.alarm_list

import androidx.compose.runtime.Immutable
import dev.abhinav.alarm.alarm.presentation.model.AlarmUi

sealed class AlarmListUIState {
    data object Loading : AlarmListUIState()
    data class Success(val alarms: List<AlarmUi>) : AlarmListUIState()
    data class Error(val message: String) : AlarmListUIState()
}

@Immutable
data class AlarmListState(
    val uiState: AlarmListUIState = AlarmListUIState.Loading
)