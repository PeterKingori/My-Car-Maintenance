package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.pkndegwa.mycarmaintenance.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

    }
}