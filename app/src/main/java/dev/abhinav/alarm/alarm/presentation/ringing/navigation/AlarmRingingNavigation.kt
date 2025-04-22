package dev.abhinav.alarm.alarm.presentation.ringing.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import dagger.hilt.android.EntryPointAccessors
import dev.abhinav.alarm.DEEPLINK_DOMAIN
import dev.abhinav.alarm.alarm.presentation.ManageAlarmUseCaseEntryPoint
import dev.abhinav.alarm.alarm.presentation.ringing.RingingScreen
import dev.abhinav.alarm.alarm.presentation.ringing.RingingScreenViewModel
import kotlinx.serialization.Serializable

@Serializable
data class AlarmRingingDestination(
    val alarmId: Int
)

fun NavGraphBuilder.alarmRingingDestination(
    onNavigateBack: () -> Unit
) {
    composable<AlarmRingingDestination>(
        deepLinks = listOf(
            navDeepLink<AlarmRingingDestination>(
                basePath = "https://$DEEPLINK_DOMAIN"
            )
        )
    ) { backStackEntry ->

        val context = LocalContext.current
        val entryPoint = EntryPointAccessors.fromApplication(context, ManageAlarmUseCaseEntryPoint::class.java)
        entryPoint.manageAlarmUseCase()

        val vm = hiltViewModel<RingingScreenViewModel>()
        val args = backStackEntry.toRoute<AlarmRingingDestination>()
        val alarmId = args.alarmId
        RingingScreen(alarmId = alarmId, onTurnOffClick = {
            vm.turnOffAlarm(alarmId)
            onNavigateBack()
        })
    }
}