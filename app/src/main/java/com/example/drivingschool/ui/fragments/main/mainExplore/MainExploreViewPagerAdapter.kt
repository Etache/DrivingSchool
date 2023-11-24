package com.example.drivingschool.ui.fragments.main.mainExplore

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.drivingschool.ui.fragments.Constants.BUNDLE_LESSON_TYPE
import com.example.drivingschool.ui.fragments.main.lesson.LessonType

class MainExploreViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainExploreFragment().apply {
                arguments = bundleOf(BUNDLE_LESSON_TYPE to LessonType.Current)
            }

            1 -> MainExploreFragment().apply {
                arguments = bundleOf(BUNDLE_LESSON_TYPE to LessonType.Previous)
            }

            else -> throw IllegalStateException("Invalid position '$position'.")
        }
    }
}