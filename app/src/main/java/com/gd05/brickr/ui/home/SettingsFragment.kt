package com.gd05.brickr.ui.home

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.gd05.brickr.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}