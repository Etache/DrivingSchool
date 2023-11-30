package com.example.drivingschool.ui.fragments.enroll.instructor.confirmEnroll

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.EnrollLessonRequest
import com.example.drivingschool.databinding.FragmentEnrollBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EnrollFragment :
    BaseFragment<FragmentEnrollBinding, EnrollViewModel>() {
    override fun getViewBinding(): FragmentEnrollBinding = FragmentEnrollBinding.inflate(layoutInflater)

    override val viewModel: EnrollViewModel by viewModels()
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private lateinit var instructorFullName: String
    private lateinit var instructorID: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEnroll.setOnClickListener {
            viewModel.enrollForLesson(
                EnrollLessonRequest(
                    instructor = instructorID,
                    date = selectedDate,
                    time = selectedTime
                )
            )
            lifecycleScope.launch {
                viewModel.enrollResponse.observe(requireActivity()) { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.viewVisibility(true)
                            binding.mainContainer.viewVisibility(false)
                        }

                        is UiState.Success -> {
                            binding.progressBar.viewVisibility(false)
                            binding.mainContainer.viewVisibility(true)
                            if (state.data?.status != null) {
                                showDialog()
                            }
                        }

                        else -> {
                            showToast("")
                        }
                    }
                }
            }
        }
    }

    override fun initialize() {
        super.initialize()
        selectedDate = arguments?.getString(Constants.TIMETABLE_TO_ENROLL_DATE).toString()
        selectedTime = arguments?.getString(Constants.TIMETABLE_TO_ENROLL_TIME).toString()
        instructorFullName = arguments?.getString(Constants.FULL_NAME).toString()
        instructorID = arguments?.getString(Constants.INSTRUCTOR_ID_ENROLL).toString()

        binding.tvDate.text = "$selectedDate, $selectedTime"
        binding.tvInstructor.text = instructorFullName
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.successfully_enrolled))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                findNavController().navigate(R.id.mainFragment)
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}