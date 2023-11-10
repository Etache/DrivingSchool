package com.example.drivingschool.ui.fragments.enroll.instructorFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentEnrollInstructorBinding
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import dagger.hilt.android.AndroidEntryPoint

class EnrollInstructorFragment :
    BaseFragment<FragmentEnrollInstructorBinding, EnrollViewModel>(R.layout.fragment_enroll_instructor) {

    override val binding by viewBinding(FragmentEnrollInstructorBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enroll_instructor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}