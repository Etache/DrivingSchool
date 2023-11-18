package com.example.drivingschool.ui.fragments.enroll.instructorFragment.enroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentEnrollInstructorBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.enroll.adapter.EnrollInstructorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
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
        getWorkWindows()
    }

    override fun initialize() {
        super.initialize()

    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding){
            btnMakeASchedule.setOnClickListener {
                if (isFridayOrSaturday()){
                    findNavController().navigate(R.id.calendarInstructorFragment)
                } else {
                    Toast.makeText(requireContext(), "сегодня не пятница и не суббота", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun isFridayOrSaturday(): Boolean {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY
    }

    private fun getWorkWindows(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currentTimetable.collect{
                    when(it){
                        is UiState.Loading -> {
                            Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Success -> {
                            adapter = EnrollInstructorAdapter(it.data?.dates, it.data?.times)
                            binding.recyclerDateAndTime.adapter = adapter
                        }
                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Empty -> {
                            Toast.makeText(requireContext(), "empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}