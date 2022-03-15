package com.oddlyspaced.bkkrht.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceManager(context: Context) {

    private companion object {
        const val APPS_KEY = "saved_apps"
    }

    private val sharedPreferences = context.getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE)

    fun saveAppsList(apps: ArrayList<String>) {
        val editor = sharedPreferences.edit()
        editor.putString(APPS_KEY, apps.joinToString("|"))
        editor.apply()
    }

    fun readAppsList(): ArrayList<String> {
        val raw = sharedPreferences.getString(APPS_KEY, "") ?: ""
        return ArrayList(raw.split("|"))
    }
}