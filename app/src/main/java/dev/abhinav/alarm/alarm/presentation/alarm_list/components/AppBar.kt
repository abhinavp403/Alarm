package dev.abhinav.alarm.alarm.presentation.alarm_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBar(title : String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF0D0F19),
        )
    )
}