package com.example.drivingschool.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.drivingschool.databinding.FragmentMainExploreBinding


class MainExploreFragment : Fragment() {

    companion object {
        const val BUNDLE_LESSON_TYPE = "bundle_media_type"
    }

    private lateinit var binding: FragmentMainExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainExploreBinding.inflate(inflater, container, false)
        return binding.root


    }

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