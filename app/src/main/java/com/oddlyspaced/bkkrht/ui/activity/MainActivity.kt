package com.oddlyspaced.bkkrht.ui.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.oddlyspaced.bkkrht.R
import com.oddlyspaced.bkkrht.databinding.ActivityMainBinding
import com.oddlyspaced.bkkrht.service.CurrentAppService
import com.oddlyspaced.bkkrht.ui.adapter.AppListAdapter
import com.oddlyspaced.bkkrht.util.SharedPreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val prefManager by lazy { SharedPreferenceManager(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appAdapter = AppListAdapter(applicationContext, prefManager.readAppsList())
        binding.rvApps.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = appAdapter
            setItemViewCacheSize(100)
        }

        binding.fabSave.setOnClickListener {
            prefManager.saveAppsList(appAdapter.getCheckedApps())
            runAfterDelay(1000L) {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isServiceEnabled()) {
            promptServiceOff()
        }
        else if (isRunningMiui()) {
            promptExtraMiuiPermission()
        }
    }

    private fun promptServiceOff() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.alert_title))
            setMessage(getString(R.string.alert_message))
            setPositiveButton(getString(R.string.alert_enable)) { _, _ ->
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            setCancelable(false)
        }.show()
    }

    private fun isServiceEnabled(): Boolean {
        val am = applicationContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (service in enabledServices) {
            if (service.resolveInfo.serviceInfo.name.contains(CurrentAppService::class.qualifiedName.toString())) {
                return true
            }
        }
        return false
    }

    @SuppressLint("PrivateApi")
    private fun isRunningMiui(): Boolean {
        val c = Class.forName("android.os.SystemProperties")
        val get = c.getMethod("get", String::class.java)
        val miui = get.invoke(c, "ro.miui.ui.version.name") as String?
        return (miui != null && miui.isNotEmpty())
    }

    private fun promptExtraMiuiPermission() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.alert_miui_title))
            setMessage(getString(R.string.alert_miui_message))
            setPositiveButton(R.string.alert_enable) {_, _ ->
                Toast.makeText(applicationContext, getString(R.string.toast_miui_alert), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }, 500L)
                Toast.makeText(applicationContext, R.string.toast_miui_alert, Toast.LENGTH_SHORT).show()
                runAfterDelay(500L) {
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    })
                }
            }
        }.show()
    }

    private fun runAfterDelay(delay: Long, methodToRun: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            methodToRun()
        }, delay)
    }

}