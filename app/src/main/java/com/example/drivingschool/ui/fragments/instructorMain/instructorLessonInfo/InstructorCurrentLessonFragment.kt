package com.example.drivingschool.ui.fragments.instructorMain.instructorLessonInfo

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentInstructorCurrentLessonBinding
import com.example.drivingschool.tools.showImage
import com.example.drivingschool.tools.showOnlyPositiveAlert
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.instructorMain.instructorLessonInfo.viewModels.ChangeLessonStatusViewModel
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.example.drivingschool.ui.fragments.studentMain.mainExplore.MainExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class InstructorCurrentLessonFragment :
    BaseFragment<FragmentInstructorCurrentLessonBinding, MainExploreViewModel>() {
    override fun getViewBinding(): FragmentInstructorCurrentLessonBinding =
        FragmentInstructorCurrentLessonBinding.inflate(layoutInflater)

    override val viewModel: MainExploreViewModel by viewModels()
    private val changeLessonStatusViewModel: ChangeLessonStatusViewModel by viewModels()

    private lateinit var lessonId: String
    private lateinit var lessonDateTime: String

    private lateinit var networkConnection: NetworkConnection

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner) {
            viewModel.getCurrentById(
                arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY)
                    ?: Constants.DEFAULT_KEY
            )
        }

        binding.ivProfileImage.setOnClickListener {
            binding.ivProfileImage.showFullSizeImage()
        }
    }

    override fun setupListeners() {
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner) {
                viewModel.getCurrentById(
                    arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY)
                        ?: Constants.DEFAULT_KEY
                )
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }

        binding.btnStartLesson.setOnClickListener {
            networkConnection.observe(viewLifecycleOwner) {
                if (isItTimeToStart(lessonDateTime)) {                    //check time to start
                    changeLessonStatusViewModel.startLesson(lessonId)
                    startLesson()
                } else {
                    showOnlyPositiveAlert(getString(R.string.lesson_start_is_not_possible_due_to_time_constraints))
                }
            }
        }
        binding.btnEndLesson.setOnClickListener {
            changeLessonStatusViewModel.finishLesson(lessonId)
            finishLesson()
        }
        binding.btnNotVisit.setOnClickListener {
            changeLessonStatusViewModel.studentAbsent(lessonId)
            studentAbsent()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupSubscribes() {
        viewModel.currentDetailsState.collectStateFlow(
            empty = {
                showToast("Empty")
            },
            loading = {
                binding.progressBar.visibility = View.VISIBLE
                binding.clContainer.visibility = View.GONE
            },
            error = {
                showToast("Error $it")
            },
            success = {
                binding.apply {
                    progressBar.visibility = View.GONE
                    clContainer.visibility = View.VISIBLE

                    tvFullname.text =
                        "${it?.student?.surname} ${it?.student?.name} ${it?.student?.lastname}"
                    tvNumber.text = it?.student?.phoneNumber
                    tvBeginningTime.text = timeWithoutSeconds(it?.time)
                    calculateEndTime(it?.time, tvEndingTime)
                    binding.tvBeginningDate.text = formatDate(it?.date)
                    binding.tvEndingDate.text = formatDate(it?.date)

                    ivProfileImage.showImage(it?.student?.profilePhoto?.big)

                    lessonDateTime = "${it?.date}, ${it?.time}"
                    lessonId = it?.id.toString()

                    if (it?.status == getString(R.string.active)) {
                        btnStartLesson.visibility = View.GONE
                        btnEndLesson.visibility = View.VISIBLE
                        btnNotVisit.visibility = View.VISIBLE
                    } else {
                        btnEndLesson.visibility = View.GONE
                        btnStartLesson.visibility = View.VISIBLE
                        btnNotVisit.visibility = View.GONE
                    }
                }
            }
        )
    }

    private fun startLesson() {
        changeLessonStatusViewModel.startLessonResult.observe(viewLifecycleOwner) {
            when (it?.status) {
                getString(R.string.success) -> {
                    showOnlyPositiveAlert(getString(R.string.your_lesson_started))
                    binding.btnStartLesson.viewVisibility(false)
                    binding.btnEndLesson.viewVisibility(true)
                    binding.btnNotVisit.viewVisibility(true)
                }

                else -> {
                }
            }
        }
    }

    private fun finishLesson() {
        changeLessonStatusViewModel.finishLessonResult.observe(viewLifecycleOwner) {
            if (it?.status == getString(R.string.success)) {
                showOnlyPositiveAlert(getString(R.string.your_lesson_ended))
                binding.btnEndLesson.visibility = View.GONE
                binding.btnNotVisit.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun studentAbsent() {
        changeLessonStatusViewModel.studentAbsentResult.observe(viewLifecycleOwner) {
            if (it?.status == getString(R.string.success)) {
                binding.btnNotVisit.visibility = View.GONE
                binding.btnEndLesson.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isItTimeToStart(dt: String): Boolean {
        val dateFormat = SimpleDateFormat(getString(R.string.yyyy_mm_dd_hh_mm_ss))
        val targetDateTime = dateFormat.parse(dt)
        val currentTime = Date()
        return if (targetDateTime != null) {
            currentTime.time > targetDateTime.time
        } else {
            false
        }
    }
}