package com.example.drivingschool.ui.fragments.instructorMain.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.databinding.FragmentInstructorMainBinding
import com.example.drivingschool.ui.fragments.instructorMain.adapter.InstructorViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class InstructorMainFragment : Fragment(R.layout.fragment_instructor_main) {

    private val binding by viewBinding(FragmentInstructorMainBinding::bind)

    private val pref: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    private val tabTitles = arrayListOf(
        "Текущие", "Предыдущие"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpInstructor.adapter = InstructorViewPagerAdapter(this@InstructorMainFragment)

        if(!pref.isLoginSuccess) {
            findNavController().navigate(R.id.loginFragment)
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setUpTabLayoutWitViewPager()
    }

    @SuppressLint("InflateParams")
    private fun setUpTabLayoutWitViewPager() {
        TabLayoutMediator(binding.tabInstructor, binding.vpInstructor) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        for (i in tabTitles.indices) {
            val textView =
                LayoutInflater.from(requireContext())
                    .inflate(/* resource = */ R.layout.tab_title, /* root = */
                        null
                    ) as TextView
            binding.tabInstructor.getTabAt(i)?.customView = textView
        }
    }
}