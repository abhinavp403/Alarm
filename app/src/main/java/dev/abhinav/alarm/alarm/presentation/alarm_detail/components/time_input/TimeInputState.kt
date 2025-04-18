package dev.abhinav.alarm.alarm.presentation.alarm_detail.components.time_input

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Stable
class TimeInputState(val hourState: TextFieldState, val minuteState: TextFieldState)

@Composable
fun rememberTimeInputState(
    initialHour: String = "",
    initialMinute: String = "",
    onValueChange: (String, String) -> Unit = { _, _ -> },
): TimeInputState {
    val hourTextInputState = rememberTextFieldState(initialHour)
    val minuteTextInputState = rememberTextFieldState(initialMinute)
    val timeInputState = remember(initialHour, initialMinute) {
        TimeInputState(hourTextInputState, minuteTextInputState)
    }

    LaunchedEffect(initialHour, initialMinute) {
        hourTextInputState.edit {
            this.replace(0, this.length, initialHour)
        }
        minuteTextInputState.edit {
            this.replace(0, this.length, initialMinute)
        }
    }

    LaunchedEffect(hourTextInputState.text, minuteTextInputState.text) {
        onValueChange(hourTextInputState.text.toString(), minuteTextInputState.text.toString())
    }

    return timeInputState
}