package dev.abhinav.alarm.alarm.presentation.alarm_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddAlarmFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick,
        modifier = modifier,
        shape = CircleShape,
        containerColor = Color(0xFF4664FF),
        contentColor = Color.White,
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add alarm",
            modifier = Modifier
                .size(60.dp)
                .padding(12.dp)
        )
    }
}