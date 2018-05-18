package com.example.joeroskopf.resume

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_taco -> displayTacoFragment()
                R.id.action_favorites -> displayFavoritesFragment()
            }
            true
        }

        bottom_navigation.setOnNavigationItemReselectedListener {
            //do nothing. The onclick listener is set to prevent the regular [OnNavigationItemSelectedListener] from
            //firing every time the user selects the item that they are already on.
        }
    }

    private fun displayFavoritesFragment() {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_mainFragment2_to_favoritesFragment)
    }

    private fun displayTacoFragment() {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_favoritesFragment_to_mainFragment2)
    }
}

