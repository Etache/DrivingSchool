package com.example.drivingschool.ui.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val tabTitles = arrayListOf(
        getString(R.string.current_lesson_text),
        getString(R.string.previous_lesson_text)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerMain.adapter = MainExploreViewPagerAdapter(this@MainFragment)

        setUpTabLayoutWitViewPager()
    }

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