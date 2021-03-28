package nl.avans.marvelapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import nl.avans.marvelapp.fragments.CharactersFragment
import nl.avans.marvelapp.fragments.ComicsFragment
import nl.avans.marvelapp.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize fragments
        val charactersFragment = CharactersFragment()
        val comicsFragment = ComicsFragment()
        val settingsFragment = SettingsFragment()

        // Set default startup fragment
        setCurrentFragment(charactersFragment)

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

    private fun setActiveNavigationItemColor(selected: MenuItem) {
        // Clear old
        val view = findViewById<BottomNavigationView>(R.id.bnvNavigationBar)
        for (i in 0 until view.menu.size()) {
            view.menu.getItem(i).isChecked = false
        }

        // Set current
        selected.isChecked = true
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }
}