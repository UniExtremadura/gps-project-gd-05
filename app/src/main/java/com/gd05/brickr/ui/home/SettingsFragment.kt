package com.gd05.brickr.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.gd05.brickr.R


class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        val DARK_MODE_KEY = "dark_mode_key"
        val TEXT_SIZE_KEY = "text_size_key"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == DARK_MODE_KEY) {
            if (sharedPreferences?.getBoolean(key, false) ?: false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        if (key == TEXT_SIZE_KEY) {
            when (sharedPreferences?.getInt(key, 1) ?: 1) {
                0 ->context?.theme?.applyStyle(R.style.Theme_Brickr_Small, true)
                1 ->context?.theme?.applyStyle(R.style.Theme_Brickr_Medium, true)
                2 ->context?.theme?.applyStyle(R.style.Theme_Brickr_Large, true)
            }
        }
    }

}