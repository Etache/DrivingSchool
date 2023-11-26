package com.example.drivingschool.ui.fragments.instructorMain.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentInstructorMainExploreBinding
import com.example.drivingschool.tools.itVisibleOtherGone
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.Constants.BUNDLE_LESSON_TYPE
import com.example.drivingschool.ui.fragments.instructorMain.adapter.InstructorLessonAdapter
import com.example.drivingschool.ui.fragments.main.lesson.LessonType
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructorMainExploreFragment :
    BaseFragment<FragmentInstructorMainExploreBinding, MainExploreViewModel>(R.layout.fragment_instructor_main_explore) {

    override val binding by viewBinding(FragmentInstructorMainExploreBinding::bind)
    override val viewModel: MainExploreViewModel by viewModels()
    private lateinit var adapter: InstructorLessonAdapter
    private var lessonType: LessonType? = null
    private lateinit var networkConnection: NetworkConnection


    @Suppress("DEPRECATION")
    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        binding.rvLessonsList.layoutManager = LinearLayoutManager(requireContext())

        networkConnection.observe(viewLifecycleOwner) {
            if (it) {
                if (lessonType == LessonType.Current) viewModel.getCurrent()
                else if (lessonType == LessonType.Previous) viewModel.getPrevious()
            }
        }

        adapter = InstructorLessonAdapter(this::onClick, requireContext(), lessonType)
        binding.rvLessonsList.adapter = adapter

        lessonType = arguments?.takeIf { it.containsKey(BUNDLE_LESSON_TYPE) }?.let {
            it.getSerializable(BUNDLE_LESSON_TYPE) as? LessonType
        }

        if (lessonType == LessonType.Current) initCurrentLessonSections()
        else if (lessonType == LessonType.Previous) initPreviousLessonSections()
        dataRefresh()
    }

    private fun onClick(id: String) {

        if (lessonType == LessonType.Current) {
            val bundle = Bundle()
            bundle.putString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY, id)
            findNavController().navigate(R.id.instructorCurrentLessonFragment, bundle)
        } else if (lessonType == LessonType.Previous) {
            val bundle = Bundle()
            bundle.putString(Constants.INSTRUCTOR_MAIN_TO_PREVIOUS_KEY, id)
            findNavController().navigate(R.id.instructorPreviousLessonFragment, bundle)
        }
    }

    private fun dataRefresh() {
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner) {
                if (it) {
                    if (lessonType == LessonType.Current) viewModel.getCurrent()
                    else if (lessonType == LessonType.Previous) viewModel.getPrevious()
                }
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }
    }

    private fun initCurrentLessonSections() {

        viewModel.currentState.collectStateFlow(
            empty = {
                binding.apply {
                    itVisibleOtherGone(noLessons, pbInstructor, rvLessonsList)
                }
            },
            loading = {
                binding.apply {
                    itVisibleOtherGone(pbInstructor, rvLessonsList, noLessons)
                }
            },
            error = {
                showToast("lessons Error: $it")
            },
            success = {
                binding.apply {
                    itVisibleOtherGone(rvLessonsList, pbInstructor, noLessons)
                    adapter.updateList(it ?: emptyList())
                }
            }
        )

    }

    private fun initPreviousLessonSections() {
        viewModel.previousState.collectStateFlow(
            empty = {
                binding.apply {
                    itVisibleOtherGone(noLessons, pbInstructor, rvLessonsList)
                }
            },
            loading = {
                binding.apply {
                    itVisibleOtherGone(pbInstructor, rvLessonsList, noLessons)
                }
            },
            error = {
                showToast("lessons Error: $it")
            },
            success = {
                binding.apply {
                    itVisibleOtherGone(rvLessonsList, pbInstructor, noLessons)
                    adapter.updateList(it ?: emptyList())
                }
            }
        )
    }
}