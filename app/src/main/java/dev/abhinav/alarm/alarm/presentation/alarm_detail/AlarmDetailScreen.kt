package dev.abhinav.alarm.alarm.presentation.alarm_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import dev.abhinav.alarm.alarm.presentation.alarm_detail.components.AlarmDetailAppBar
import dev.abhinav.alarm.alarm.presentation.alarm_detail.components.AlarmFieldChangeDialog
import dev.abhinav.alarm.alarm.presentation.alarm_detail.components.AlarmSetting
import dev.abhinav.alarm.alarm.presentation.alarm_detail.components.time_input.AlarmTimeInput
import dev.abhinav.alarm.alarm.presentation.alarm_detail.components.time_input.rememberTimeInputState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDetailScreen(
    modifier: Modifier = Modifier,
    alarmId: Int? = null,
    state: AlarmDetailState,
    onAction: (AlarmDetailAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(alarmId) {
        onAction(AlarmDetailAction.OnLoadAlarm(alarmId))
    }

    val timeInputState = rememberTimeInputState(state.hourField, state.minuteField) { hour, minute ->
            onAction(AlarmDetailAction.OnTimeChange(hour, minute))
        }

    if (state.showAlarmNameDialog) {
        AlarmFieldChangeDialog(
            onSaveClicked = { onAction(AlarmDetailAction.OnSaveAlarmName(it)) },
            fieldText = state.alarm?.title ?: "",
            onDismiss = {
                onAction(AlarmDetailAction.OnDismissAlarmNameDialog)
            },
            modifier = Modifier.systemBarsPadding()
        )
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                AlarmDetailAppBar(
                    isAlarmValid = state.isAlarmValid,
                    onBackClick = {
                        keyboardController?.hide()
                        onAction(AlarmDetailAction.OnBackClick)
                    },
                    onSaveClick = {
                        keyboardController?.hide()
                        onAction(AlarmDetailAction.OnSaveClick)
                    },
                    modifier = Modifier
                )
            },
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 16.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AlarmTimeInput(
                state = timeInputState
            )
            AlarmSetting(
                title = "Alarm Name",
                onClick = {
                    onAction(AlarmDetailAction.OnAlarmNameClick)
                },
                trailingContent = {
                    Text(state.alarmName ?: "N/A", style = MaterialTheme.typography.bodyMedium)
                }
            )
        }
    }
}