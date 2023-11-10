package com.example.drivingschool.ui.fragments.enroll.selectDate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentSelectDateTimeBinding
import com.example.drivingschool.ui.fragments.enroll.selectInstructor.SelectInstructorFragment

class SelectDateTimeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_date_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val workWindows = arguments?.get(SelectInstructorFragment.WORK_WINDOWS_KEY)
        val fullName = arguments?.getSerializable(SelectInstructorFragment.FULL_NAME)
        Log.d("madimadi", "name: $fullName work_windows: $workWindows")
    }
}