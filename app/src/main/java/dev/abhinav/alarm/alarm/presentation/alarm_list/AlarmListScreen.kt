package dev.abhinav.alarm.alarm.presentation.alarm_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.abhinav.alarm.alarm.presentation.alarm_list.components.AddAlarmFab
import dev.abhinav.alarm.alarm.presentation.alarm_list.components.AlarmList
import dev.abhinav.alarm.alarm.presentation.alarm_list.components.AppBar
import dev.abhinav.alarm.alarm.presentation.alarm_list.components.EmptyData

@Composable
fun AlarmListScreen(
    state: AlarmListState,
    onAction: (AlarmListAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AddAlarmFab(
                onClick = {
                    onAction(AlarmListAction.OnAddNewAlarmClick)
                },
                modifier = Modifier
                    .padding(bottom = 20.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .consumeWindowInsets(paddingValues)
            ) {
                AppBar(
                    title = "Your Alarms",
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .shadow(
                            elevation = 10.dp,
                            spotColor = Color(0xFFF6F6F6),
                            clip = true,
                            ambientColor = Color(0xFFF6F6F6),
                            shape = CircleShape
                        )
                )

                when (val uiState = state.uiState) {
                    is AlarmListUIState.Loading -> {
                        // Loading
                    }
                    is AlarmListUIState.Success -> {
                        // Success
                        if (uiState.alarms.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .navigationBarsPadding()
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                EmptyData(
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                        } else {
                            Box {
                                AlarmList(
                                    alarms = uiState.alarms,
                                    onAlarmClick = { alarm ->
                                        onAction(AlarmListAction.OnAlarmClick(alarm.id!!))
                                    },
                                    onAlarmSwitchClick = { alarm, isChecked ->
                                        onAction(
                                            AlarmListAction.OnAlarmSwitchClick(
                                                alarm.id!!,
                                                isChecked
                                            )
                                        )
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                                LandingHeader()
                            }
                        }
                    }
                    is AlarmListUIState.Error -> {
                        // Error
                    }
                }
            }
        }
    )
}

@Composable
fun LandingHeader() {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .height(40.dp)
            .background(
                brush = Brush.linearGradient(
                    0.0f to MaterialTheme.colorScheme.background,
                    1.0f to Color.Transparent,
                    start = Offset(0.0f, 0.0f),
                    end = Offset(0.0f, 100.0f)
                )
            )
    )
}
