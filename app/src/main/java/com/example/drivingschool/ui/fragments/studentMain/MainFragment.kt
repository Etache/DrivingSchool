package com.example.drivingschool.ui.fragments.studentMain

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.databinding.FragmentMainBinding
import com.example.drivingschool.ui.fragments.studentMain.mainExplore.MainExploreViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private val pref: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerMain.adapter = MainExploreViewPagerAdapter(this@MainFragment)
        binding.viewPagerMain.isSaveEnabled = true

        if (!pref.isLoginSuccess || pref.accessToken == null) { //check token
            findNavController().navigate(R.id.loginFragment)
        }
        if (pref.role == getString(R.string.instructor)){
            findNavController().navigate(R.id.instructorMainFragment)
        } else {
            setUpTabLayoutWitViewPager()
        }
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private val tabTitles = arrayListOf(
        "Текущие", "Предыдущие"
    )

    @SuppressLint("InflateParams")
    private fun setUpTabLayoutWitViewPager() {
        TabLayoutMediator(binding.tabLayoutMain, binding.viewPagerMain) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        for (i in tabTitles.indices) {
            val textView =
                LayoutInflater.from(requireContext())
                    .inflate(/* resource = */ R.layout.tab_title, /* root = */
                        null
                    ) as TextView
            binding.tabLayoutMain.getTabAt(i)?.customView = textView
        }
    }
}