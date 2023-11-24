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
import com.example.drivingschool.ui.fragments.Constants.EFCIFCURRENTWEEKEMPTY
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
    private var currentSchedule: List<String>? = null
    private var nextSchedule: List<String>? = null

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
        with(binding) {
            btnMakeASchedule.setOnClickListener {
                val isCurrentWeekEmpty = currentSchedule.isNullOrEmpty()
                val isNextWeekEmpty = nextSchedule.isNullOrEmpty()
                if ((isFridayOrSaturday() && isNextWeekEmpty) || currentSchedule.isNullOrEmpty()) {
                    val bundle = Bundle()
                    bundle.putBoolean(EFCIFCURRENTWEEKEMPTY, isCurrentWeekEmpty)
                    findNavController().navigate(R.id.calendarInstructorFragment, bundle)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "сегодня не пятница и не суббота или же у вас есть расписание на следующую неделю",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun isFridayOrSaturday(): Boolean {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
    }

    private fun getWorkWindows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentTimetable.collect {
                    when (it) {
                        is UiState.Loading -> {
                        }

                        is UiState.Success -> {
                            val currentWeekDates: List<String> = mutableListOf<String>().apply {
                                it.data?.currentWeek?.forEach { currentWeek ->
                                    currentWeek.date?.let { add(it) }
                                }
                            }

                            val nextWeekDates: List<String> = mutableListOf<String>().apply {
                                it.data?.nextWeek?.forEach { nextWeek ->
                                    nextWeek.date?.let { add(it) }
                                }
                            }
                            nextSchedule = nextWeekDates

                            adapter = EnrollInstructorAdapter(it.data?.currentWeek)
                            binding.recyclerDateAndTime.adapter = adapter
                            currentSchedule = currentWeekDates
                        }

                        is UiState.Error -> {
                        }

                        is UiState.Empty -> {
                        }
                    }
                }
            }
        }
    }
}