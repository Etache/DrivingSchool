package com.example.drivingschool.ui.fragments.instructorMain.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentInstructorMainExploreBinding
import com.example.drivingschool.tools.itVisibleOtherGone
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.Constants.BUNDLE_LESSON_TYPE
import com.example.drivingschool.ui.fragments.instructorMain.adapter.InstructorLessonAdapter
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.example.drivingschool.ui.fragments.studentMain.lesson.LessonType
import com.example.drivingschool.ui.fragments.studentMain.mainExplore.MainExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructorMainExploreFragment :
    BaseFragment<FragmentInstructorMainExploreBinding, MainExploreViewModel>() {
    override fun getViewBinding(): FragmentInstructorMainExploreBinding = FragmentInstructorMainExploreBinding.inflate(layoutInflater)

    override val viewModel: MainExploreViewModel by viewModels()

    private lateinit var adapter: InstructorLessonAdapter
    private var lessonType: LessonType? = null
    private lateinit var networkConnection: NetworkConnection


    @Suppress("DEPRECATION")
    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        binding.rvMainExplore.layoutManager = LinearLayoutManager(requireContext())

        networkConnection.observe(viewLifecycleOwner) {
            if (it) {
                if (lessonType == LessonType.Current) viewModel.getCurrent()
                else if (lessonType == LessonType.Previous) viewModel.getPrevious()
            }
        }

        adapter = InstructorLessonAdapter(this::onClick, requireContext(), lessonType)
        binding.rvMainExplore.adapter = adapter

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
        binding.swipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner) {
                if (it) {
                    if (lessonType == LessonType.Current) viewModel.getCurrent()
                    else if (lessonType == LessonType.Previous) viewModel.getPrevious()
                }
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initCurrentLessonSections() {
        viewModel.currentState.collectStateFlow(
            empty = {
                binding.apply {
                    itVisibleOtherGone(viewNoLessons, mainProgressBar, rvMainExplore)
                }
            },
            loading = {
                binding.apply {
                    itVisibleOtherGone(mainProgressBar, rvMainExplore, viewNoLessons)
                }
            },
            error = {
                showToast("lessons Error: $it")
            },
            success = {
                binding.apply {
                    if (it?.isNotEmpty() == true) {
                        itVisibleOtherGone(rvMainExplore, mainProgressBar, viewNoLessons)
                        Log.e(
                            "ololo",
                            "initCurrentLessonSections: UiState.Success $it"
                        )
                        adapter.updateList(sortDataByDateTime(it, LessonType.Current))
                    } else {
                        itVisibleOtherGone(viewNoLessons, mainProgressBar, rvMainExplore)
                    }
                }
            }
        )
    }

    private fun initPreviousLessonSections() {
        viewModel.previousState.collectStateFlow(
            empty = {
                binding.apply {
                    itVisibleOtherGone(viewNoLessons, mainProgressBar, rvMainExplore)
                    tvNoLessons.text = getString(R.string.text_no_lesson_previous)
                }
            },
            loading = {
                binding.apply {
                    itVisibleOtherGone(mainProgressBar, rvMainExplore, viewNoLessons)
                }
            },
            error = {
                showToast("lessons Error: $it")
            },
            success = {
                binding.apply {
                    if (it?.isNotEmpty() == true) {
                        itVisibleOtherGone(rvMainExplore, mainProgressBar, viewNoLessons)
                        Log.e(
                            "ololo",
                            "initCurrentLessonSections: UiState.Success $it"
                        )
                        adapter.updateList(sortDataByDateTime(it, LessonType.Previous))
                    } else {
                        itVisibleOtherGone(viewNoLessons, rvMainExplore, mainProgressBar)
                    }
                    tvNoLessons.text = getString(R.string.text_no_lesson_previous)
                }
            }
        )
    }
}