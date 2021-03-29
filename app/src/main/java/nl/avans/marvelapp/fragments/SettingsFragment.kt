package nl.avans.marvelapp.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import nl.avans.marvelapp.MainActivity
import nl.avans.marvelapp.R


class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "language" -> changeLanguage()
        }
    }

    //region Helpers
    private fun changeLanguage() {
        recreateActivity()
    }

    private fun recreateActivity() {
        val myActivity: MainActivity = requireContext() as MainActivity
        myActivity.recreate()
    }
    //endregion Helpers

}