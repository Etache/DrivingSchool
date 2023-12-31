package com.example.drivingschool.ui.fragments.enroll.instructor.checkTable

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.InstructorWorkWindowRequest
import com.example.drivingschool.databinding.FragmentCheckTimetableBinding
import com.example.drivingschool.ui.activity.MainActivity
import com.example.drivingschool.ui.fragments.Constants.CTFEFARRAYDATES
import com.example.drivingschool.ui.fragments.Constants.CTFEFARRAYTIMES
import com.example.drivingschool.ui.fragments.enroll.instructor.checkTable.adapter.CheckTimetableAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckTimetableFragment :
    BaseFragment<FragmentCheckTimetableBinding, CheckTimetableViewModel>() {
    override fun getViewBinding(): FragmentCheckTimetableBinding =
        FragmentCheckTimetableBinding.inflate(layoutInflater)

    override val viewModel: CheckTimetableViewModel by viewModels()
    lateinit var adapter: CheckTimetableAdapter
    private lateinit var instructorWorkWindowCreate: InstructorWorkWindowRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        instructorWorkWindowCreate = InstructorWorkWindowRequest(
            arguments?.getStringArrayList(CTFEFARRAYDATES),
            arguments?.getStringArrayList(CTFEFARRAYTIMES)
        )
        adapter = CheckTimetableAdapter(
            instructorWorkWindowCreate.date as ArrayList<String>,
            instructorWorkWindowCreate.time as ArrayList<String>,
        )
        binding.recyclerDateAndTimeTimetable.adapter = adapter
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.btnConfirmTimetable.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.schedule_has_been_drawn_up))
            builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                createTimetable(instructorWorkWindowCreate)
                startActivity(intent)
                dialog.cancel()
            }.create().show()
        }
    }

    private fun createTimetable(instructorWorkWindowRequest: InstructorWorkWindowRequest) {
        instructorWorkWindowRequest.date = instructorWorkWindowCreate.date
        instructorWorkWindowRequest.time = instructorWorkWindowCreate.time
        viewModel.setWorkWindows(instructorWorkWindowRequest)
        viewModel.checkTimetable.observe(viewLifecycleOwner) {
        }
    }
}