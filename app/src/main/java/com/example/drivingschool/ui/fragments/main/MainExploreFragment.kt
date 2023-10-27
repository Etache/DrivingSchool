package com.example.drivingschool.ui.fragments.main

import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentMainExploreBinding


class MainExploreFragment :
    BaseFragment<FragmentMainExploreBinding, MainExploreViewModel>(R.layout.fragment_main_explore) {

    companion object {
        const val BUNDLE_LESSON_TYPE = "bundle_media_type"
    }

    override val binding by viewBinding(FragmentMainExploreBinding::bind)
    override val viewModel: MainExploreViewModel
        get() = TODO("Not yet implemented")

    override fun initialize() {
        
        @Suppress("DEPRECATION")
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