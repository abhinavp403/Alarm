package dev.abhinav.alarm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

const val DEEPLINK_DOMAIN = "vordead.com"

@HiltAndroidApp
open class MainApplication : Application()