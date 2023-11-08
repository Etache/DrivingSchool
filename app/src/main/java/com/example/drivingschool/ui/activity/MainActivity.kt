package com.example.drivingschool.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.drivingschool.R
import com.example.drivingschool.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navView = binding.navView
        setSupportActionBar(binding.myToolbar)
        setAppBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController(R.id.nav_host_fragment).navigateUp()
            Toast.makeText(this, "Home button pressed", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun setAppBar() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment,
                R.id.selectInstructorFragment,
                R.id.studentProfileFragment,
                R.id.instructorProfileFragment,
                R.id.instructorInfoFragment,
                R.id.currentLessonDetailsFragment,
                R.id.previousLessonDetailsFragment,
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = when (destination.id) {
                R.id.mainFragment -> "Главная страница"
                R.id.currentLessonDetailsFragment -> "Текущие"
                R.id.previousLessonDetailsFragment -> "Предыдущие"
                 R.id.currentLessonDetailsFragment -> "Главная страница"
                R.id.previousLessonDetailsFragment -> "Главная страница"
                R.id.selectInstructorFragment -> "Онлайн запись"
                R.id.instructorInfoFragment -> "Онлайн запись"
                R.id.studentProfileFragment -> "Профиль"
                R.id.instructorProfileFragment -> "Профиль"
                else -> "No title"
            }
            if(destination.id == R.id.loginFragment) {
                supportActionBar?.hide()
                navView.isVisible = false
            } else {
                supportActionBar?.show()
                navView.isVisible = true
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}