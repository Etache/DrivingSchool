package com.example.drivingschool.ui.fragments.instructorMain.adapter

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.drivingschool.ui.fragments.instructorMain.fragments.InstructorMainExploreFragment
import com.example.drivingschool.ui.fragments.main.lesson.LessonType

class InstructorViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(
        fragment.childFragmentManager,
        fragment.viewLifecycleOwner.lifecycle
    ) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InstructorMainExploreFragment().apply {
                arguments = bundleOf(InstructorMainExploreFragment.BUNDLE_LESSON_TYPE to LessonType.Current)
            }
            1 -> InstructorMainExploreFragment().apply {
                arguments = bundleOf(InstructorMainExploreFragment.BUNDLE_LESSON_TYPE to LessonType.Previous)
            }
            else -> throw IllegalStateException("Invalid position: $position")
        }
        Log.d("ahahaha", "данные на InstructorViewPager")
    }
}