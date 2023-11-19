package com.example.drivingschool.ui.fragments.enroll.confirmEnroll

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.EnrollLessonRequest
import com.example.drivingschool.databinding.FragmentEnrollBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.BundleKeys
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.UUID

@AndroidEntryPoint
class EnrollFragment : BaseFragment<FragmentEnrollBinding, EnrollViewModel>(R.layout.fragment_enroll) {

    override val binding by viewBinding(FragmentEnrollBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()
    private lateinit var selectedDate : String
    private lateinit var selectedTime : String
    private lateinit var instructorFullName : String
    private lateinit var instructorID : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enroll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEnroll.setOnClickListener{
            viewModel.enrollForLesson(EnrollLessonRequest(instructor = instructorID, date = selectedDate, time = selectedTime))
            lifecycleScope.launch {
                Log.e("madimadi", "id: ${instructorID}")
                Log.e("madimadi", "date: ${selectedDate}")
                Log.e("madimadi", "time: ${selectedTime}")
                viewModel.enrollResponse.observe(requireActivity()){state->
                    when(state) {
                        is UiState.Loading -> {
                            Toast.makeText(requireContext(), "state is loading", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Success -> {
                            if (state.data?.success != null) {
                                showDialog()
                            }
                            Toast.makeText(requireContext(), "${state.data?.success}", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(requireContext(), "state is not success", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun initialize() {
        super.initialize()
        selectedDate = arguments?.getString(BundleKeys.TIMETABLE_TO_ENROLL_DATE).toString()
        selectedTime = arguments?.getString(BundleKeys.TIMETABLE_TO_ENROLL_TIME).toString()
        instructorFullName = arguments?.getString(BundleKeys.FULL_NAME).toString()
        instructorID = arguments?.getString(BundleKeys.INSTRUCTOR_ID_ENROLL).toString()
        Log.d("madimadi", "date in enrollFragment: $selectedDate")
        Log.d("madimadi", "time in enrollFragment: $selectedTime")
        Log.d("madimadi", "instructorID in enrollFragment: $instructorID")

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

    fun convertToBoundary() {
        val boundary = "Boundary-${UUID.randomUUID()}"
        val mediaType = "multipart/form-data; boundary=$boundary".toMediaTypeOrNull()
        val requestBody = MultipartBody.Builder(boundary)
            .setType(MultipartBody.FORM)
            .addFormDataPart("instructor", "")
            .addFormDataPart("date", selectedDate)
            .addFormDataPart("time", selectedTime)
            .addFormDataPart(
                "file",
                "path/to/your/file",
                RequestBody.create("application/octet-stream".toMediaTypeOrNull(), File("path/to/your/file"))
            )
            .build()
    }
}