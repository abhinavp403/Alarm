package dev.abhinav.alarm

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.abhinav.alarm.alarm.presentation.alarm_detail.navigation.alarmDetailDestination
import dev.abhinav.alarm.alarm.presentation.alarm_detail.navigation.navigateToAlarmDetail
import dev.abhinav.alarm.alarm.presentation.alarm_list.navigation.AlarmListNavigation
import dev.abhinav.alarm.alarm.presentation.alarm_list.navigation.alarmListDestination
import dev.abhinav.alarm.alarm.presentation.ringing.navigation.alarmRingingDestination
import dev.abhinav.alarm.ui.theme.AlarmTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.USE_FULL_SCREEN_INTENT,
                    android.Manifest.permission.USE_EXACT_ALARM,
                    android.Manifest.permission.SCHEDULE_EXACT_ALARM
                ),
                1
            )
        }

        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            AlarmTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { innerPadding ->
                    AlarmNavHost(
                        modifier = Modifier
                            .consumeWindowInsets(innerPadding)
                            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
                    )
                }
            }
        }
    }

    @Composable
    fun AlarmNavHost(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = AlarmListNavigation,
            modifier = modifier,
        ) {
            alarmListDestination(onNavigateToAlarmDetail = { alarmId ->
                navController.navigateToAlarmDetail(alarmId)
            })
            alarmDetailDestination(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToRingtoneSettings = {}
            )

            alarmRingingDestination(onNavigateBack = {
                navController.navigateUp()
            })
        }
    }
}