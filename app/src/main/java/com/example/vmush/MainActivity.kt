package com.example.vmush

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.vmush.Pages.Fragment.HomeFragment
import com.example.vmush.Pages.Fragment.PermintaanFragment
import com.example.vmush.Pages.Fragment.Profile_Fragment
import com.example.vmush.ui.CalendarFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment())

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> loadFragment(HomeFragment())
                R.id.navigation_profile -> loadFragment(Profile_Fragment())
                R.id.navigation_requests -> loadFragment(PermintaanFragment())
                R.id.navigation_calendar -> loadFragment(CalendarFragment())
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main, fragment)
            .commit()
        return true
    }
}
