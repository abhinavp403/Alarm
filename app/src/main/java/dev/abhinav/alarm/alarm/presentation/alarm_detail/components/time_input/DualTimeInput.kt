package dev.abhinav.alarm.alarm.presentation.alarm_detail.components.time_input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.abhinav.alarm.R
import dev.abhinav.alarm.alarm.presentation.alarm_detail.util.hourInput
import dev.abhinav.alarm.alarm.presentation.alarm_detail.util.minuteInput
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun AlarmTimeInput(
    state: TimeInputState,
    modifier: Modifier = Modifier,
) {

    var shouldShowTimeLeft by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf("") }

    LaunchedEffect(state.hourState.text, state.minuteState.text) {
        shouldShowTimeLeft =
            state.hourState.text.isNotEmpty() && state.minuteState.text.isNotEmpty()
        if (shouldShowTimeLeft) {
            val hour = state.hourState.text.toString().toIntOrNull() ?: 0
            val minute = state.minuteState.text.toString().toIntOrNull() ?: 0
            remainingTime = calculateRemainingTime(hour, minute)
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimeInputField(
                    state = state.hourState,
                    inputTransformation = InputTransformation.hourInput(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_colon),
                    contentDescription = "Colon",
                    tint = Color(0xFF858585),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                TimeInputField(
                    state = state.minuteState,
                    inputTransformation = InputTransformation.minuteInput(),
                    modifier = Modifier
                )
            }

            AnimatedVisibility(
                visible = shouldShowTimeLeft,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    "Alarm in $remainingTime",
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }
    }
}

fun calculateRemainingTime(hour: Int, minute: Int): String {
    val now = LocalDateTime.now().withSecond(0).withNano(0)
    val alarmTime = LocalTime.of(hour, minute)
    val alarmDateTime =
        if (alarmTime.isAfter(now.toLocalTime()) || alarmTime == now.toLocalTime()) {
            now.withHour(alarmTime.hour).withMinute(alarmTime.minute)
        } else {
            now.plusDays(1).withHour(alarmTime.hour).withMinute(alarmTime.minute)
        }

    val duration = java.time.Duration.between(now, alarmDateTime)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}min"
        else -> "${minutes}min"
    }
}