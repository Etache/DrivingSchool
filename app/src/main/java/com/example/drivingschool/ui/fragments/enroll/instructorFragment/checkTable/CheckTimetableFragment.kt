package com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.IsntructorWorkWindow
import com.example.drivingschool.databinding.FragmentCheckTimetableBinding
import com.example.drivingschool.ui.activity.MainActivity
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.CalendarInstructorFragment.Companion.CTFEFARRAYDATES
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.CalendarInstructorFragment.Companion.CTFEFARRAYTIMES
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable.adapter.CheckTimetableAdapter

class CheckTimetableFragment :
    BaseFragment<FragmentCheckTimetableBinding, CheckTimetableViewModel>(R.layout.fragment_check_timetable) {

    override val binding by viewBinding(FragmentCheckTimetableBinding::bind)
    override val viewModel: CheckTimetableViewModel by viewModels()
    lateinit var adapter: CheckTimetableAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments
        val instructorWorkWindow = IsntructorWorkWindow(
            arguments?.getStringArrayList(CTFEFARRAYDATES),
            arguments?.getStringArrayList(CTFEFARRAYTIMES)
        )
        adapter = CheckTimetableAdapter(instructorWorkWindow.date,instructorWorkWindow.time)
        binding.recyclerDateAndTime.adapter = adapter
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.btnConfirmTimetable.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Расписание составлено")
            builder.setPositiveButton("Ok") { dialog, which ->
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dialog.cancel()
            }.create().show()
        }
    }
}