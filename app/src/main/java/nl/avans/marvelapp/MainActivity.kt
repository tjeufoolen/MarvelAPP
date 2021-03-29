package nl.avans.marvelapp

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import nl.avans.marvelapp.fragments.CharactersFragment
import nl.avans.marvelapp.fragments.ComicsFragment
import nl.avans.marvelapp.fragments.SettingsFragment
import nl.avans.marvelapp.utils.ContextUtils
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private var currentFragment: Fragment? = null
    }

    private lateinit var charactersFragment: CharactersFragment
    private lateinit var comicsFragment: ComicsFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(PreferenceManager.getDefaultSharedPreferences(newBase)
            .getString("language", "en")!!)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize fragments
        charactersFragment = CharactersFragment()
        comicsFragment = ComicsFragment()
        settingsFragment = SettingsFragment()

        // Set default startup fragment
        if (currentFragment == null) {
            setCurrentFragment(charactersFragment)
        }

        // Handle bottom bar navigation
        findViewById<BottomNavigationView>(R.id.bnvNavigationBar).setOnNavigationItemSelectedListener {
            setActiveNavigationItemColor(it)

            when(it.itemId) {
                R.id.iCharacters -> setCurrentFragment(charactersFragment)
                R.id.iComics -> setCurrentFragment(comicsFragment)
                R.id.iSettings -> setCurrentFragment(settingsFragment)
            }

            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        currentFragment = fragment
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    private fun clearNavigationItemsColor() {
        val view = findViewById<BottomNavigationView>(R.id.bnvNavigationBar)
        for (i in 0 until view.menu.size()) {
            view.menu.getItem(i).isChecked = false
        }
    }

    private fun setActiveNavigationItemColor(selected: MenuItem) {
        // Clear old
       clearNavigationItemsColor()

        // Set current
        selected.isChecked = true
    }
}