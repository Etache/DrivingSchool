package com.example.drivingschool.ui.fragments.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentMainExploreBinding


class MainExploreFragment : Fragment(R.layout.fragment_main_explore) {

    companion object {
        const val BUNDLE_LESSON_TYPE = "bundle_media_type"
    }

    private val binding by viewBinding(FragmentMainExploreBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lessonType = arguments?.takeIf { it.containsKey(BUNDLE_LESSON_TYPE) }?.let {
            it.getSerializable(BUNDLE_LESSON_TYPE) as? LessonType
        }

        if (lessonType == LessonType.Current) initCurrentLessonSections()
        else if (lessonType == LessonType.Previous) initPreviousLessonSections()

        binding.rvMainExplore.adapter = LessonAdapter()

    }

    //Для проверки вида
    private fun initPreviousLessonSections() {
        binding.apply {
            rvMainExplore.isVisible = true
            viewNoLessons.isVisible = false
        }
    }

    //Для проверки вида
    private fun initCurrentLessonSections() {
        binding.apply {
            rvMainExplore.isVisible = false
            viewNoLessons.isVisible = true
        }
    }

}