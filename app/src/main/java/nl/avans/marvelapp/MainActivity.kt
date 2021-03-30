package nl.avans.marvelapp

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import nl.avans.marvelapp.fragments.AccountFragment
import nl.avans.marvelapp.fragments.CharactersFragment
import nl.avans.marvelapp.fragments.ComicsFragment
import nl.avans.marvelapp.fragments.SettingsFragment
import nl.avans.marvelapp.models.Account
import nl.avans.marvelapp.utils.ContextUtils
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        var account: Account? = null

        private var currentFragment: Fragment? = null
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var charactersFragment: CharactersFragment
    private lateinit var comicsFragment: ComicsFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var accountFragment: AccountFragment

    override fun attachBaseContext(newBase: Context?) {
        val localeToSwitchTo = Locale(
            PreferenceManager.getDefaultSharedPreferences(newBase)
            .getString("language", "en")!!)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase!!, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize instance variables
        drawerLayout = findViewById(R.id.dlDrawerContainer)
        navigationView = findViewById(R.id.nvDrawerNavigation)
        bottomNavigationView = findViewById(R.id.bnvNavigationBar)
        toolbar = findViewById(R.id.tAppBar)
        charactersFragment = CharactersFragment()
        comicsFragment = ComicsFragment()
        settingsFragment = SettingsFragment()
        accountFragment = AccountFragment()

        // Set custom action bar
        setSupportActionBar(toolbar)

        // Set default account (placeholder)
        if (account == null) {
            account = Account(1, "Avans Gebruiker", "info@avans.nl")
        }

        // Setup drawer navigation
        setupDrawerNavigation()

        // Set default startup fragment
        if (currentFragment == null) {
            setCurrentFragment(charactersFragment)
        }

        // Handle drawer navigation
        navigationView.setNavigationItemSelectedListener {
            setCurrentFragment(it)
            setActiveNavigationItemColor(it, false)
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Handle bottom bar navigation
        bottomNavigationView.setOnNavigationItemSelectedListener {
            setCurrentFragment(it)
            setActiveNavigationItemColor(it)
            true
        }
    }

    fun updateAccountInformation() {
        val container = navigationView.getHeaderView(0)

        if (account != null) {
            val picture = container.findViewById<ImageView>(R.id.ivHeaderProfilePicture)
            val name = container.findViewById<TextView>(R.id.tvHeaderAccountName)
            val email = container.findViewById<TextView>(R.id.tvHeaderAccountEmail)

            name.text = account?.name
            email.text = account?.email
        }
    }

    private fun setupDrawerNavigation() {
        navigationView.bringToFront()

        // Add click listener
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Init account information
        updateAccountInformation()
    }

    private fun setCurrentFragment(it: MenuItem) {
        when(it.itemId) {
            R.id.iCharacters -> setCurrentFragment(charactersFragment)
            R.id.iComics -> setCurrentFragment(comicsFragment)
            R.id.iSettings -> setCurrentFragment(settingsFragment)
            R.id.iAccount -> setCurrentFragment(accountFragment)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        currentFragment = fragment

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    private fun onBackPressed(fm: FragmentManager?): Boolean {
        if (fm != null) {
            if (fm.backStackEntryCount > 0) {
                fm.popBackStack()
                return true
            }
            val fragList: List<Fragment> = fm.fragments
            if (fragList.isNotEmpty()) {
                for (frag in fragList) {
                    if (frag.isVisible) {
                        if (onBackPressed(frag.childFragmentManager)) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }
    
    private fun clearNavigationItemsColor() {
        for (i in 0 until bottomNavigationView.menu.size()) {
            bottomNavigationView.menu.getItem(i).isChecked = false
        }
    }

    private fun setActiveNavigationItemColor(selected: MenuItem, fromBottomNav: Boolean = true) {
        // Clear old
        clearNavigationItemsColor()

        // Set current
        if (fromBottomNav) {
            selected.isChecked = true
            return
        }

        when (currentFragment) {
            is CharactersFragment -> bottomNavigationView.menu[0].isChecked = true
            is ComicsFragment ->     bottomNavigationView.menu[1].isChecked = true
            is SettingsFragment ->   bottomNavigationView.menu[2].isChecked = true
            else ->                  bottomNavigationView.menu[3].isChecked = true
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val fm = supportFragmentManager
            if (onBackPressed(fm)) {
                return
            }
            super.onBackPressed()
        }
    }
}