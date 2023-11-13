package com.example.drivingschool.ui.fragments.main.mainExplore

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentMainExploreBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.BundleKeys.BUNDLE_LESSON_TYPE
import com.example.drivingschool.ui.fragments.BundleKeys.MAIN_TO_CURRENT_KEY
import com.example.drivingschool.ui.fragments.BundleKeys.MAIN_TO_PREVIOUS_KEY
import com.example.drivingschool.ui.fragments.main.lesson.LessonAdapter
import com.example.drivingschool.ui.fragments.main.lesson.LessonType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainExploreFragment :
    BaseFragment<FragmentMainExploreBinding, MainExploreViewModel>(R.layout.fragment_main_explore) {

    override val binding by viewBinding(FragmentMainExploreBinding::bind)
    override val viewModel: MainExploreViewModel by viewModels()

    private lateinit var adapter: LessonAdapter
    private var lessonType: LessonType? = null

    override fun initialize() {

        @Suppress("DEPRECATION")
        lessonType = arguments?.takeIf { it.containsKey(BUNDLE_LESSON_TYPE) }?.let {
            it.getSerializable(BUNDLE_LESSON_TYPE) as? LessonType
        }

        adapter = LessonAdapter(this::onClick, requireContext(), lessonType)
        binding.rvMainExplore.adapter = adapter

        if (lessonType == LessonType.Current) initCurrentLessonSections()
        else if (lessonType == LessonType.Previous) initPreviousLessonSections()

        binding.swipeRefresh.setOnRefreshListener {
            dataRefresh()
            binding.swipeRefresh.isRefreshing = false
        }

    }

    override fun onStart() {
        super.onStart()
        dataRefresh()
    }
    private fun dataRefresh() {
        if (lessonType == LessonType.Current) viewModel.getCurrent()
        else if (lessonType == LessonType.Previous) viewModel.getPrevious()
    }

    private fun onClick(id: String) {

        if (lessonType == LessonType.Current) {
            val bundle = Bundle()
            bundle.putString(MAIN_TO_CURRENT_KEY, id)
            findNavController().navigate(R.id.currentLessonDetailsFragment, bundle)
        } else if (lessonType == LessonType.Previous) {
            val bundle = Bundle()
            bundle.putString(MAIN_TO_PREVIOUS_KEY, id)
            findNavController().navigate(R.id.previousLessonDetailsFragment, bundle)
        }

    }

    private fun initCurrentLessonSections() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentState.collect {
                    when (it) {
                        is UiState.Empty -> {
                            binding.apply {
                                mainProgressBar.viewVisibility(false)
                                rvMainExplore.viewVisibility(false)
                                viewNoLessons.viewVisibility(true)
                            }
                        }

                        is UiState.Error -> {
                            showToast(it.msg.toString())
                        }

                        is UiState.Loading -> {
                            binding.apply {
                                mainProgressBar.viewVisibility(true)
                                rvMainExplore.viewVisibility(false)
                                viewNoLessons.viewVisibility(false)
                            }

                        }

                        is UiState.Success -> {
                            binding.apply {
                                mainProgressBar.viewVisibility(false)
                                rvMainExplore.viewVisibility(true)
                                viewNoLessons.viewVisibility(false)
                                adapter.updateList(it.data ?: emptyList())
                            }

                        }
                    }
                }
            }
        }
    }

    private fun initPreviousLessonSections() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.previousState.collect {
                    when (it) {
                        is UiState.Empty -> {
                            binding.apply {
                                mainProgressBar.viewVisibility(false)
                                rvMainExplore.viewVisibility(false)
                                viewNoLessons.viewVisibility(true)
                            }
                        }

                        is UiState.Error -> {
                            showToast(it.msg.toString())
                        }

                        is UiState.Loading -> {
                            binding.apply {
                                mainProgressBar.viewVisibility(true)
                                rvMainExplore.viewVisibility(false)
                                viewNoLessons.viewVisibility(false)
                            }

                        }

                        is UiState.Success -> {
                            binding.apply {
                                mainProgressBar.viewVisibility(false)
                                rvMainExplore.viewVisibility(true)
                                viewNoLessons.viewVisibility(false)

                                adapter.updateList(it.data ?: emptyList())
                            }

                        }
                    }
                }
            }
        }
    }


}