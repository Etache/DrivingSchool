package com.example.drivingschool.ui.fragments.instructorMain.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.drivingschool.ui.fragments.instructorMain.fragments.InstructorMainExploreFragment
import com.example.drivingschool.ui.fragments.main.lesson.LessonType
import com.example.drivingschool.ui.fragments.BundleKeys.BUNDLE_LESSON_TYPE


class InstructorViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InstructorMainExploreFragment().apply {
                arguments = bundleOf(BUNDLE_LESSON_TYPE to LessonType.Current)
            }
            1 -> InstructorMainExploreFragment().apply {
                arguments = bundleOf(BUNDLE_LESSON_TYPE to LessonType.Previous)
            }
            else -> throw IllegalStateException("Invalid position '$position'.")
        }
    }
}