package com.oddlyspaced.bkkrht.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.oddlyspaced.bkkrht.databinding.ActivityBhaiBinding

class BhaiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBhaiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBhaiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}