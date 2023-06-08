package ru.dsvusial.playlistmaker.root.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.dsvusial.playlistmaker.R

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        if (savedInstanceState == null) {

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.setupWithNavController(navController)


        }
    }
}