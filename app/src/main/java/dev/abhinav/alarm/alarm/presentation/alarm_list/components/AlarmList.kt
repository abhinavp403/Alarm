package dev.abhinav.alarm.alarm.presentation.alarm_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.abhinav.alarm.alarm.presentation.model.AlarmUi

@Composable
fun AlarmList(
    alarms: List<AlarmUi>,
    onAlarmClick: (AlarmUi) -> Unit,
    onAlarmSwitchClick: (AlarmUi, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(alarms, key = { alarm -> alarm.hashCode() }) { alarm ->
            AlarmListItem(
                alarmUi = alarm,
                onAlarmClick = {
                    onAlarmClick(alarm)
                },
                onAlarmSwitchClick = { isChecked ->
                    onAlarmSwitchClick(alarm, isChecked)
                }
            )
        }
    }
}