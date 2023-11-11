package com.example.drivingschool.ui.fragments.enroll.selectDate

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentSelectDateTimeBinding
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel

class SelectDateTimeFragment :
    BaseFragment<FragmentSelectDateTimeBinding, EnrollViewModel>(R.layout.fragment_select_date_time){

    override val binding by viewBinding(FragmentSelectDateTimeBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_date_time, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}