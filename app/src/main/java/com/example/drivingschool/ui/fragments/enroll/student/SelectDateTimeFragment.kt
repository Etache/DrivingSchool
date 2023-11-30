package com.example.drivingschool.ui.fragments.enroll.student

import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.Date
import com.example.drivingschool.data.models.TimeInWorkWindows
import com.example.drivingschool.databinding.FragmentSelectDateTimeBinding
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.instructor.calendar.customCalendar.CalendarWeekDayFormatter
import com.example.drivingschool.ui.fragments.enroll.student.adapter.TimeAdapter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class SelectDateTimeFragment :
    BaseFragment<FragmentSelectDateTimeBinding, EnrollViewModel>() {
    override fun getViewBinding(): FragmentSelectDateTimeBinding = FragmentSelectDateTimeBinding.inflate(layoutInflater)

    override val viewModel: EnrollViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    private val adapter = TimeAdapter(this::onTimeClick)
    private lateinit var workWindows: ArrayList<Date>
    private lateinit var instructorFullName: String
    private var instructorID: String? = null
    private var selectedFinalDate: String? = null
    private var selectedFinalTime: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workWindows = arguments?.getSerializable(Constants.WORK_WINDOWS) as ArrayList<Date>
        instructorFullName = arguments?.getString(Constants.FULL_NAME).toString()
        instructorID = arguments?.getString(Constants.INSTRUCTOR_ID_ENROLL).toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initialize() {
        super.initialize()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        val spacing = 2
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(spacing))
        with(binding) {
            val today = CalendarDay.today()
            calendarView.setDateSelected(today, false)
            calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE
            calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getStringArray(R.array.mcv_monthLabels)))
            val customWeekDayFormatter = CalendarWeekDayFormatter()
            calendarView.setWeekDayFormatter(customWeekDayFormatter)
            calendarView.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)
            cantGoBackMonth()
            setGrayDaysDecorator(calendarView)
            calendarView.setDateSelected(today, false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupListeners() {
        super.setupListeners()
        binding.btnConfirm.setOnClickListener {
            if (selectedFinalTime != null && selectedFinalDate != null) {
                val bundle = Bundle()
                bundle.putString(Constants.TIMETABLE_TO_ENROLL_DATE, selectedFinalDate)
                bundle.putString(Constants.TIMETABLE_TO_ENROLL_TIME, selectedFinalTime)
                bundle.putString(Constants.FULL_NAME, instructorFullName)
                bundle.putString(Constants.INSTRUCTOR_ID_ENROLL, instructorID)
                findNavController().navigate(R.id.enrollFragment, bundle)
            } else {
                showToast("Выберите время")
            }

        }
        binding.calendarView.setOnDateChangedListener { _, _, _ ->
            val selectedDate = binding.calendarView.selectedDate.date.toString()
            val inputDateFormat =
                SimpleDateFormat(getString(R.string.eee_mmm_dd_hh_mm_ss_gmt_z_yyyy), Locale.US)
            val date = inputDateFormat.parse(selectedDate)
            val outputDateFormat = SimpleDateFormat(getString(R.string.yyyy_mm_dd), Locale.US)
            val outputDateString = outputDateFormat.format(date!!)

            selectedFinalDate = outputDateString //saveSelectedDate

            workWindows.forEach { dates ->
                if (dates.date == outputDateString) {
                    adapter.setTimesList(dates.times!!)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTimeClick(time: TimeInWorkWindows?) {
        selectedFinalTime = if(time != null){
            val oldTime =
                LocalTime.parse(time.time, DateTimeFormatter.ofPattern(getString(R.string.hh_mm)))
            oldTime.format(DateTimeFormatter.ofPattern(getString(R.string.hh_mm_ss)))
        } else {
            null
        }
    }

    private fun setGrayDaysDecorator(calendarView: MaterialCalendarView) {

        val grayDaysDecorator = object : DayViewDecorator {

            override fun shouldDecorate(day: CalendarDay): Boolean {
                return !isDayActive(day)
            }

            override fun decorate(view: DayViewFacade) {
                view.addSpan(
                    ForegroundColorSpan(
                        resources.getColor(R.color.gray)
                    )
                )
                view.setDaysDisabled(true)
            }
        }
        calendarView.addDecorator(grayDaysDecorator)
    }

    private fun isDayActive(day: CalendarDay): Boolean {
        var isActive = false
        val inputDateFormat =
            SimpleDateFormat(getString(R.string.eee_mmm_dd_hh_mm_ss_gmt_z_yyyy), Locale.US)
        val parsedDate = inputDateFormat.parse(day.date.toString())
        val outputDateFormat = SimpleDateFormat(getString(R.string.yyyy_mm_dd), Locale.US)
        val outputDateString = outputDateFormat.format(parsedDate!!)
        workWindows.forEach { date ->
            if (date.date == outputDateString) {
                isActive = true
            }
        }
        return isActive
    }

    private fun cantGoBackMonth() {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.DAY_OF_MONTH, 1)
        binding.calendarView.state().edit().setMinimumDate(CalendarDay.from(currentDate)).commit()
        val today = CalendarDay.today()
        binding.calendarView.setDateSelected(today, true)
    }
}