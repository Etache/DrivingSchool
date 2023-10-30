package com.example.drivingschool.ui.fragments.mainDetails

import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentCurrentLessonDetailBinding

class CurrentLessonDetailsFragment() :
    BaseFragment<FragmentCurrentLessonDetailBinding, CurrentLessonDetailsViewModel>(R.layout.fragment_current_lesson_detail) {

    override val binding by viewBinding(FragmentCurrentLessonDetailBinding::bind)
    override val viewModel: CurrentLessonDetailsViewModel
        get() = TODO("Not yet implemented")


}