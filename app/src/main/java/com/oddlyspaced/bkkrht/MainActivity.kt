package com.oddlyspaced.bkkrht

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.oddlyspaced.bkkrht.databinding.ActivityMainBinding

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
}