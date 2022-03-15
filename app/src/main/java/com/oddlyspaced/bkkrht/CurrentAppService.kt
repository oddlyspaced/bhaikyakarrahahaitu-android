package com.oddlyspaced.bkkrht

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class CurrentAppService: AccessibilityService() {

    companion object {
        const val TAG = "CurrentAppService"
    }

    private var currentFocusedPackage = ""

    override fun onServiceConnected() {
        Log.d(TAG, "Service Connected!")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Log.d(TAG, "Accessibility Event!")
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.packageName != null && event.className != null) {
                val componentName = ComponentName(event.packageName.toString(), event.className.toString())
                val activityInfo = getActivityName(componentName)
                activityInfo?.let { info ->
                    val tempPackageName = info.packageName.trim()
                    if (tempPackageName.isNotEmpty() && currentFocusedPackage != tempPackageName) {
                        Log.d(TAG, "----------------------------------")
                        Log.d(TAG, tempPackageName)
                        Log.d(TAG, "----------------------------------")
                        currentFocusedPackage = tempPackageName
                    }
                }
            }
        }
    }

    private fun getActivityName(componentName: ComponentName): ActivityInfo? {
        return try {
            packageManager.getActivityInfo(componentName, 0)
        }
        catch (e: PackageManager.NameNotFoundException) {
            // e.printStackTrace()
            null
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service Interrupted!")
    }
}