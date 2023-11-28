package com.example.drivingschool.ui.fragments.instructorMain.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.internal.findRootView
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.databinding.FragmentInstructorMainBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.instructorMain.adapter.InstructorViewPagerAdapter
import com.example.drivingschool.ui.fragments.studentMain.mainExplore.MainExploreViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructorMainFragment : BaseFragment<FragmentInstructorMainBinding, MainExploreViewModel>() {

    override val viewModel: MainExploreViewModel by viewModels()
    override fun getViewBinding(): FragmentInstructorMainBinding =
        FragmentInstructorMainBinding.inflate(layoutInflater)

    private val pref: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    private val tabTitles = arrayListOf(
        "Текущие", "Предыдущие"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vpInstructor.adapter = InstructorViewPagerAdapter(this@InstructorMainFragment)
        binding.vpInstructor.isSaveEnabled = false

        if (!pref.isLoginSuccess) {
            findNavController().navigate(R.id.loginFragment)
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setUpTabLayoutWitViewPager()
        checkNotifications()
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

    private fun checkNotifications() {
        if (pref.role == getString(R.string.instructor)) {
            val drawableIcon: View = requireActivity().findViewById(R.id.notificationIcon)
            viewModel.checkNotifications()
            viewModel.notificationCheck.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Success -> {
                        if (state.data?.is_notification == true) {
                            drawableIcon.setBackgroundResource(R.drawable.ic_new_notification)
                        } else {
                            drawableIcon.setBackgroundResource(R.drawable.ic_notification)
                        }
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Empty -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.empty_state),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    else -> {
                        //todo
                    }
                }
            }
        }
    }
}