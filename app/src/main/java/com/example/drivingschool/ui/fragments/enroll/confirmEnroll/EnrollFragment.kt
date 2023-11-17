package com.example.drivingschool.ui.fragments.enroll.confirmEnroll

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentEnrollBinding
import com.example.drivingschool.ui.fragments.BundleKeys
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel


class EnrollFragment : BaseFragment<FragmentEnrollBinding, EnrollViewModel>(R.layout.fragment_enroll) {

    override val binding by viewBinding(FragmentEnrollBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()
    private lateinit var selectedDate : String
    private lateinit var selectedTime : String
    private lateinit var instructorFullName : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enroll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEnroll.setOnClickListener{
            //query to enroll
            showDialog()
        }
    }

    override fun initialize() {
        super.initialize()
        selectedDate = arguments?.getString(BundleKeys.TIMETABLE_TO_ENROLL_DATE).toString()
        selectedTime = arguments?.getString(BundleKeys.TIMETABLE_TO_ENROLL_TIME).toString()
        instructorFullName = arguments?.getString(BundleKeys.FULL_NAME).toString()
        Log.d("madimadi", "date in enrollFragment: $selectedDate")
        Log.d("madimadi", "time in enrollFragment: $selectedTime")

        binding.tvDate.text = "${selectedDate}, ${selectedTime}"
        binding.tvInstructor.text = instructorFullName
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Запись забронирована")
            .setPositiveButton("Oк", DialogInterface.OnClickListener { dialog, id ->
                findNavController().navigate(R.id.mainFragment)
                dialog.cancel()
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}