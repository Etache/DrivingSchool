package com.example.drivingschool.ui.fragments.instructorMain.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.ui.fragments.BundleKeys.BUNDLE_LESSON_TYPE
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentInstructorMainExploreBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.BundleKeys
import com.example.drivingschool.ui.fragments.instructorMain.adapter.InstructorLessonAdapter
import com.example.drivingschool.ui.fragments.main.lesson.LessonType
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        networkConnection.observe(viewLifecycleOwner){
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
            bundle.putString("key", id)
            findNavController().navigate(R.id.instructorCurrentLessonFragment, bundle)
        }
        else if (lessonType == LessonType.Previous) {
            val bundle = Bundle()
            bundle.putString(BundleKeys.INSTRUCTOR_MAIN_TO_PREVIOUS_KEY, id)
            findNavController().navigate(R.id.instructorPreviousLessonFragment, bundle)
        }
    }

    private fun dataRefresh() {
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner){
                if (it) {
                    if (lessonType == LessonType.Current) viewModel.getCurrent()
                    else if (lessonType == LessonType.Previous) viewModel.getPrevious()
                }
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }
    }

    private fun initCurrentLessonSections() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentState.collect {
                    when (it) {
                        is UiState.Loading -> {
                            binding.apply {
                                pbInstructor.viewVisibility(true)
                                rvLessonsList.viewVisibility(false)
                                noLessons.viewVisibility(false)
                            }
                            Log.d("ahahaha", "InstructorMainExploreFragment initCurrentLessonSection Loading работает")
                        }

                        is UiState.Empty -> {
                            binding.apply {
                                pbInstructor.viewVisibility(false)
                                rvLessonsList.viewVisibility(false)
                                noLessons.viewVisibility(true)
                            }
                            Log.d("ahahaha", "InstructorMainExploreFragment initCurrentLessonSection Empty работает")
                        }

                        is UiState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "lessons error: ${it.msg}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("ahahaha", "InstructorMainExploreFragment initCurrentLessonSection Error работает")
                        }

                        is UiState.Success -> {
                            binding.apply {
                                pbInstructor.viewVisibility(false)
                                rvLessonsList.viewVisibility(true)
                                noLessons.viewVisibility(false)
                                adapter.updateList(it.data ?: emptyList())
                            }
                            Log.d("ahahaha", "${it.data}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun initPreviousLessonSections() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.previousState.collect {
                    Log.d("ahahaha", "initPreviousLessonSection Работает")
                    when (it) {
                        is UiState.Loading -> {
                            binding.apply {
                                pbInstructor.viewVisibility(true)
                                rvLessonsList.viewVisibility(false)
                                noLessons.viewVisibility(false)
                            }
                        }

                        is UiState.Empty -> {
                            binding.apply {
                                pbInstructor.viewVisibility(false)
                                rvLessonsList.viewVisibility(false)
                                noLessons.viewVisibility(true)
                            }
                        }

                        is UiState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "lessons error: ${it.msg}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is UiState.Success -> {
                            binding.apply {
                                pbInstructor.viewVisibility(false)
                                rvLessonsList.viewVisibility(true)
                                noLessons.viewVisibility(false)
                                adapter.updateList(it.data ?: emptyList())
                            }
                            Log.d("ahahaha", "initPreviousLessonSections: ${it.data}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}