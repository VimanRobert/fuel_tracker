package com.fueltracker.presentation.utils

import android.content.Context
import android.content.pm.PackageManager

object SystemHandler {

    fun isAutomotive(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)
    }

    fun isMobile(context: Context): Boolean {
        return !isAutomotive(context)
    }
}