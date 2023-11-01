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
import com.example.drivingschool.ui.fragments.main.lesson.LessonAdapter
import com.example.drivingschool.ui.fragments.main.lesson.LessonType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainExploreFragment :
    BaseFragment<FragmentMainExploreBinding, MainExploreViewModel>(R.layout.fragment_main_explore) {

    companion object {
        const val BUNDLE_LESSON_TYPE = "bundle_media_type"
    }

    override val binding by viewBinding(FragmentMainExploreBinding::bind)
    override val viewModel: MainExploreViewModel by viewModels()

    private lateinit var adapter: LessonAdapter

    override fun initialize() {

        adapter = LessonAdapter(this::onClick)
        binding.rvMainExplore.adapter = adapter

        @Suppress("DEPRECATION")
        val lessonType = arguments?.takeIf { it.containsKey(BUNDLE_LESSON_TYPE) }?.let {
            it.getSerializable(BUNDLE_LESSON_TYPE) as? LessonType
        }

        if (lessonType == LessonType.Current) initCurrentLessonSections()
        else if (lessonType == LessonType.Previous) initPreviousLessonSections()

    }

    private fun onClick(id: String) {
        val bundle = Bundle()
        bundle.putString("key", id)
        findNavController().navigate(R.id.currentLessonDetailsFragment, bundle)
    }

    //Для проверки вида
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
                            showToast(it.msg)
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


    //Для проверки вида
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
                            showToast(it.msg)
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