package dev.abhinav.alarm.alarm.presentation.alarm_detail.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.abhinav.alarm.alarm.presentation.alarm_detail.AlarmDetailEvent
import dev.abhinav.alarm.alarm.presentation.alarm_detail.AlarmDetailScreen
import dev.abhinav.alarm.alarm.presentation.alarm_detail.AlarmDetailViewModel
import dev.abhinav.alarm.core.presentation.util.ObserveAsEvents
import kotlinx.serialization.Serializable

@Serializable
data class AlarmDetail(val id: Int? = null)

fun NavController.navigateToAlarmDetail(id: Int?) = navigate(AlarmDetail(id))


fun NavGraphBuilder.alarmDetailDestination(
    onNavigateBack: () -> Unit,
    onNavigateToRingtoneSettings: () -> Unit,
) {
    composable<AlarmDetail>(
        enterTransition = {
            slideIntoContainer(Up)
        },
        exitTransition = {
            slideOutOfContainer(Down)
        }
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<AlarmDetail>()
        val vm: AlarmDetailViewModel = hiltViewModel()
        val uiState = vm.uiState.collectAsStateWithLifecycle()

        ObserveAsEvents(
            events = vm.alarmDetailEvents,
            onEvent = { event ->
                when (event) {
                    is AlarmDetailEvent.NavigateBack -> onNavigateBack()
                }
            }
        )

        AlarmDetailScreen(
            alarmId = args.id,
            state = uiState.value,
            onAction = vm::onAction
        )
    }
}