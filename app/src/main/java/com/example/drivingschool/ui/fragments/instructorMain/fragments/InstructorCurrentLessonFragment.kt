package com.example.drivingschool.ui.fragments.instructorMain.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentInstructorCurrentLessonBinding
import com.example.drivingschool.ui.fragments.enroll.adapter.SelectInstructorAdapter
import com.example.drivingschool.ui.fragments.instructorMain.adapter.InstructorLessonAdapter
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
import kotlinx.coroutines.launch

class InstructorCurrentLessonFragment : Fragment() {

    private lateinit var binding: FragmentInstructorCurrentLessonBinding
    private val viewModel: MainExploreViewModel by viewModels()
    private var id: Int ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructorCurrentLessonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments?.getInt(InstructorLessonAdapter.ID_KEY)

        getCurrentLessonInfo()
    }

    private fun getCurrentLessonInfo() {
        viewModel.currentState
    }
}