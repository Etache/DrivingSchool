package com.example.drivingschool.ui.fragments.studentMain.mainExplore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.databinding.FragmentMainExploreBinding
import com.example.drivingschool.tools.itVisibleOtherGone
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.Constants.BUNDLE_LESSON_TYPE
import com.example.drivingschool.ui.fragments.Constants.MAIN_TO_CURRENT_KEY
import com.example.drivingschool.ui.fragments.Constants.MAIN_TO_PREVIOUS_KEY
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.example.drivingschool.ui.fragments.studentMain.lesson.LessonAdapter
import com.example.drivingschool.ui.fragments.studentMain.lesson.LessonType
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class MainExploreFragment :
    BaseFragment<FragmentMainExploreBinding, MainExploreViewModel>() {

    override fun getViewBinding(): FragmentMainExploreBinding =
        FragmentMainExploreBinding.inflate(layoutInflater)

    override val viewModel: MainExploreViewModel by viewModels()

    private lateinit var adapter: LessonAdapter
    private var lessonType: LessonType? = null
    private lateinit var networkConnection: NetworkConnection

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        dataRefresh()

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

    private fun dataRefresh() {
        networkConnection.observe(viewLifecycleOwner) {
            if (it) {
                if (lessonType == LessonType.Current) viewModel.getCurrent()
                else if (lessonType == LessonType.Previous) viewModel.getPrevious()
            }
        }
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

        viewModel.currentState.collectStateFlow(
            empty = {
                binding.apply {
                    itVisibleOtherGone(viewNoLessons, rvMainExplore, mainProgressBar)
                }
            },
            loading = {
                binding.apply {
                    itVisibleOtherGone(mainProgressBar, rvMainExplore, viewNoLessons)
                }
            },
            error = {
                showToast(it)
            },
            success = {
                binding.apply {
                    if (it?.isNotEmpty() == true) {
                        itVisibleOtherGone(rvMainExplore, mainProgressBar, viewNoLessons)
                        Log.e(
                            "ololo",
                            "initCurrentLessonSections: UiState.Success $it"
                        )
                        adapter.updateList(sortDataByDateTime(it))
                    } else {
                        itVisibleOtherGone(viewNoLessons, rvMainExplore, mainProgressBar)
                    }
                }
            }
        )
    }

    private fun initPreviousLessonSections() {
        viewModel.previousState.collectStateFlow(
            empty = {
                binding.apply {
                    itVisibleOtherGone(viewNoLessons, rvMainExplore, mainProgressBar)
                    tvNoLessons.text = getString(R.string.text_no_lesson_previous)
                }
            },
            loading = {
                binding.apply {
                    itVisibleOtherGone(mainProgressBar, rvMainExplore, viewNoLessons)
                }
            },
            error = {
                showToast(it)
            },
            success = { it ->
                binding.apply {
                    if (it?.isNotEmpty() == true) {
                        itVisibleOtherGone(rvMainExplore, mainProgressBar, viewNoLessons)
                        Log.e(
                            "ololo",
                            "initCurrentLessonSections: UiState.Success $it"
                        )
                        adapter.updateList(sortDataByDateTime(it))
                        Log.e("ololo", "Before Sorting: $it")
                        Log.e("ololo", "After Sorting: ${sortDataByDateTime(it)}")
                    } else {
                        itVisibleOtherGone(viewNoLessons, rvMainExplore, mainProgressBar)
                    }
                }
            }
        )
    }

    private fun sortDataByDateTime(data: List<LessonsItem>): List<LessonsItem> {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val formattedData = data.map { customData ->
            Pair(sdf.parse("${customData.date} ${customData.time}"), customData)
        }

        val sortedData = formattedData.sortedBy { it.first }

        return sortedData.map { it.second }
    }
}