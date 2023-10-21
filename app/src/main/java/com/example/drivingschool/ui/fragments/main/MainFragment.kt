package com.example.drivingschool.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drivingschool.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerMain.adapter = MainExploreViewPagerAdapter(this@MainFragment)

        TabLayoutMediator(binding.tabLayoutMain, binding.viewPagerMain) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Текущие"
                }

                1 -> {
                    tab.text = "Предыдущие"
                }
            }
        }.attach()
    }


}