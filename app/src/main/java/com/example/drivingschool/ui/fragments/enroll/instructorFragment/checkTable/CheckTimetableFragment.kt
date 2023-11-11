package com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentCheckTimetableBinding
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
        adapter = CheckTimetableAdapter()
        binding.recyclerDateAndTime.adapter = adapter
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.btnConfirmTimetable.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Расписание составлено")
            builder.setPositiveButton("Ok",DialogInterface.OnClickListener { dialog, which ->
                findNavController().navigate(R.id.mainFragment)
            }).create().show()
        }
    }
}