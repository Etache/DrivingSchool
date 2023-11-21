package com.example.drivingschool.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.databinding.ActivityMainBinding
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.login.CheckRoleCallBack
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CheckRoleCallBack {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navigation: NavGraph
    private lateinit var networkConnection: NetworkConnection
    //
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkConnection()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navView = binding.navView

        val inflater = navHostFragment.navController.navInflater
        navigation = inflater.inflate(R.navigation.nav_graph)

        setSupportActionBar(binding.myToolbar)

        setAppBar()
        checkRole()
    }

    override fun checkRole() {
        if(preferences.role == "instructor"){
            navView.menu.clear() //clear old inflated items.
            navView.inflateMenu(R.menu.instructor_bottom_nav_menu)
            navigation.setStartDestination(R.id.instructorMainFragment)
            navController.navigate(R.id.instructorMainFragment)
        } else if (preferences.role == "student"){
            navView.menu.clear()
            navView.inflateMenu(R.menu.bottom_nav_menu)
            navigation.setStartDestination(R.id.mainFragment)
            navController.navigate(R.id.mainFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController(R.id.nav_host_fragment).navigateUp()
        }
        return true
    }

    private fun setAppBar() {
        //
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment,
               // R.id.enrollFragment,
               // R.id.selectInstructorFragment,
                R.id.studentProfileFragment,
                R.id.instructorProfileFragment,
               // R.id.instructorInfoFragment,
               // R.id.currentLessonDetailsFragment,
               // R.id.previousLessonDetailsFragment,
                R.id.selectInstructorFragment,
                //R.id.checkTimetableFragment,
                R.id.enrollInstructorFragment,
               // R.id.selectDateTimeFragment,
                R.id.instructorMainFragment
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = when (destination.id) {
                R.id.mainFragment -> "Главная страница"
                R.id.currentLessonDetailsFragment -> "Текущее занятие"//
                R.id.previousLessonDetailsFragment -> "Предыдущее занятие"//
                R.id.selectInstructorFragment -> "Онлайн запись"///
                R.id.enrollFragment -> "Онлайн запись"//
                R.id.checkTimetableFragment -> "Расписание"//
                R.id.enrollInstructorFragment -> "Расписание"
                R.id.calendarInstructorFragment -> "Расписание"//
                R.id.instructorInfoFragment -> "Онлайн запись"//
                R.id.selectDateTimeFragment -> "Онлайн запись"//
                R.id.studentProfileFragment -> "Профиль"
                R.id.instructorProfileFragment -> "Профиль"
                R.id.instructorMainFragment -> "Главная страница"
                R.id.instructorCurrentLessonFragment -> "Текущее занятие"//
                R.id.instructorPreviousLessonFragment -> "Предыдущее занятие"//
                else -> "No title"
            }
            if (destination.id == R.id.loginFragment) {
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

    //
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkConnection() {
        networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this){
            if (!it) {
                binding.contentNoInternet.root.viewVisibility(true)
                binding.navView.viewVisibility(false)
            }
        }

        binding.contentNoInternet.btnCheckInternet.setOnClickListener{
            binding.contentNoInternet.apply {
                progressBar.viewVisibility(true)
                tvCheckInternet.viewVisibility(false)
                btnCheckInternet.viewVisibility(false)
                showAnimation()
                progressBar.postDelayed({
                    var state = false
                    progressBar.animation.cancel()
                    progressBar.viewVisibility(false)
                    networkConnection.observe(this@MainActivity){state = it}
                    if (state) {
                        binding.contentNoInternet.root.viewVisibility(false)
                        binding.navView.viewVisibility(true)
                    }
                    tvCheckInternet.viewVisibility(true)
                    btnCheckInternet.viewVisibility(true)
                }, 3000)
            }
        }
    }

    private fun showAnimation() {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.duration = 1700
        binding.contentNoInternet.progressBar.startAnimation(rotateAnimation)
    }

}