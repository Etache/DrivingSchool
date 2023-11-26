package com.example.drivingschool.ui.fragments.instructorLessonInfo

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentInstructorCurrentLessonBinding
import com.example.drivingschool.tools.showImage
import com.example.drivingschool.tools.showOnlyPositiveAlert
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.instructorLessonInfo.viewModels.ChangeLessonStatusViewModel
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InstructorCurrentLessonFragment :
    BaseFragment<FragmentInstructorCurrentLessonBinding, MainExploreViewModel>(R.layout.fragment_instructor_current_lesson) {

    override val binding by viewBinding(FragmentInstructorCurrentLessonBinding::bind)
    override val viewModel: MainExploreViewModel by viewModels()
    private val changeLessonStatusViewModel: ChangeLessonStatusViewModel by viewModels()

    private lateinit var lessonId: String
    private lateinit var lessonDateTime: String

    private lateinit var networkConnection: NetworkConnection

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        Log.e(
            "ololololo",
            "initialize: ${arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY)}"
        )
        networkConnection.observe(viewLifecycleOwner) {
            viewModel.getCurrentById(
                arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY)
                    ?: Constants.DEFAULT_KEY
            )
        }

        binding.ivProfileImage.showFullSizeImage()
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
            changeLessonStatusViewModel.startLesson(lessonId)
            startLesson()
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
                Log.e(
                    "ahahaha",
                    "InstructorCurrentLessonFragment Success: $it",
                )

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

                    lessonDateTime = "${it?.date} ${timeWithoutSeconds(it?.time)}"
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
        //проверка на начало нет
        changeLessonStatusViewModel.startLessonResult.observe(viewLifecycleOwner, Observer {
            if (it?.status == getString(R.string.success)) {
                showOnlyPositiveAlert(getString(R.string.your_lesson_started))

                binding.btnStartLesson.visibility = View.GONE
                binding.btnEndLesson.visibility = View.VISIBLE
                binding.btnNotVisit.visibility = View.VISIBLE
            } else if (it?.status == getString(R.string.error)) {
                showOnlyPositiveAlert(getString(R.string.lesson_start_is_not_possible_due_to_time_constraints))
            } else {
                Log.e("ahahaha", "startLesson: ${it?.status}")
            }
        })
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
}