package com.example.drivingschool.ui.fragments.enroll.instructor.enroll

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentEnrollInstructorBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.activity.MainActivity
import com.example.drivingschool.ui.fragments.Constants.EFCIFCURRENTWEEKEMPTY
import com.example.drivingschool.ui.fragments.enroll.instructor.enroll.adapter.CurrentEnrollInstructorAdapter
import com.example.drivingschool.ui.fragments.enroll.instructor.enroll.adapter.NextEnrollInstructorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class EnrollInstructorFragment :
    BaseFragment<FragmentEnrollInstructorBinding, EnrollInstructorViewModel>() {
    override fun getViewBinding(): FragmentEnrollInstructorBinding =
        FragmentEnrollInstructorBinding.inflate(layoutInflater)

    override val viewModel: EnrollInstructorViewModel by viewModels()
    private lateinit var currentAdapter: CurrentEnrollInstructorAdapter
    private lateinit var nextAdapter: NextEnrollInstructorAdapter
    private var currentSchedule: List<String>? = null
    private var nextSchedule: List<String>? = null

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
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Нельзя составить расписание")
                    builder.setMessage(
                        "Новое расписание можно составить при условии, что у вас еще нет расписания " +
                                "на следующую неделю и сегодня день недели с пятницы по воскресенье"
                    )
                    builder.setPositiveButton("Ok") { dialog, which ->
                        dialog.cancel()
                    }.create().show()
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
                            closeViews()
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
                            currentSchedule = currentWeekDates
                            currentAdapter = CurrentEnrollInstructorAdapter(it.data?.currentWeek)
                            binding.recyclerDateAndTimeCurrentWeek.adapter = currentAdapter
                            nextAdapter = NextEnrollInstructorAdapter(it.data?.nextWeek)
                            binding.recyclerDateAndTimeNextWeek.adapter = nextAdapter
                            openViews()
                        }

                        is UiState.Error -> {
                            showToast(it.msg.toString())
                        }

                        is UiState.Empty -> {
                            showToast(getString(R.string.empty_state))
                        }
                    }
                }
            }
        }
    }

    private fun closeViews() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            tvDateAndTimeEnrollCurrentWeek.visibility = View.GONE
            recyclerDateAndTimeCurrentWeek.visibility = View.GONE
            tvDateAndTimeEnrollNextWeek.visibility = View.GONE
            recyclerDateAndTimeNextWeek.visibility = View.GONE
            btnMakeASchedule.visibility = View.GONE
        }
    }

    private fun openViews() {
        with(binding) {
            progressBar.visibility = View.GONE
            if (currentSchedule.isNullOrEmpty() && nextSchedule.isNullOrEmpty()){
                viewNoSchedule.visibility = View.VISIBLE
            } else {
                viewNoSchedule.visibility = View.GONE
                tvDateAndTimeEnrollCurrentWeek.visibility = View.VISIBLE
                recyclerDateAndTimeCurrentWeek.visibility = View.VISIBLE
                tvDateAndTimeEnrollNextWeek.visibility = View.VISIBLE
                recyclerDateAndTimeNextWeek.visibility = View.VISIBLE
                btnMakeASchedule.visibility = View.VISIBLE
            }
        }
    }
}