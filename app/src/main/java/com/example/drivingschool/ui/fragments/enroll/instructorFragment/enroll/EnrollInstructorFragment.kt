package com.example.drivingschool.ui.fragments.enroll.instructorFragment.enroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentEnrollInstructorBinding
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.enroll.adapter.EnrollInstructorAdapter

class EnrollInstructorFragment :
    BaseFragment<FragmentEnrollInstructorBinding, EnrollInstructorViewModel>(R.layout.fragment_enroll_instructor) {

    override val binding by viewBinding(FragmentEnrollInstructorBinding::bind)
    override val viewModel: EnrollInstructorViewModel by viewModels()
    private lateinit var adapter: EnrollInstructorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enroll_instructor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun initialize() {
        super.initialize()
        adapter = EnrollInstructorAdapter()
        binding.recyclerDateAndTime.adapter = adapter
    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding){
            btnMakeASchedule.setOnClickListener {
                findNavController().navigate(R.id.calendarInstructorFragment)
            }
        }
    }
}