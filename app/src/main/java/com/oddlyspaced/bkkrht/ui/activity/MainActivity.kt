package com.oddlyspaced.bkkrht.ui.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.oddlyspaced.bkkrht.util.SharedPreferenceManager
import com.oddlyspaced.bkkrht.databinding.ActivityMainBinding
import com.oddlyspaced.bkkrht.service.CurrentAppService
import com.oddlyspaced.bkkrht.ui.adapter.AppListAdapter

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
        }

        binding.fabSave.setOnClickListener {
            prefManager.saveAppsList(appAdapter.getCheckedApps())
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isServiceEnabled()) {
            promptServiceOff()
        }
    }

    private fun promptServiceOff() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Service Off!")
            setMessage("To let Ashneer Bhai remove your dogalapan, turn on the service for this application")
            setPositiveButton("Enable") { _, _ ->
                Toast.makeText(applicationContext, "Enable toggle for service under ", Toast.LENGTH_SHORT).show()
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
}