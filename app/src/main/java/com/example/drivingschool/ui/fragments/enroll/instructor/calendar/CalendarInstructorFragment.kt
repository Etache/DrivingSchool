package com.example.drivingschool.ui.fragments.enroll.instructor.calendar

import android.os.Bundle
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.Times
import com.example.drivingschool.databinding.FragmentCalendarInstructorBinding
import com.example.drivingschool.ui.fragments.Constants.ADAPTER_STATE
import com.example.drivingschool.ui.fragments.Constants.CTFEFARRAYDATES
import com.example.drivingschool.ui.fragments.Constants.CTFEFARRAYTIMES
import com.example.drivingschool.ui.fragments.Constants.LIST_OF_DATES
import com.example.drivingschool.ui.fragments.Constants.LIST_OF_TIMES
import com.example.drivingschool.ui.fragments.enroll.instructor.calendar.adapter.CalendarInstructorAdapter
import com.example.drivingschool.ui.fragments.enroll.instructor.calendar.customCalendar.CalendarWeekDayFormatter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarInstructorFragment :
    BaseFragment<FragmentCalendarInstructorBinding, CalendarInstructorViewModel>() {
    override fun getViewBinding(): FragmentCalendarInstructorBinding =
        FragmentCalendarInstructorBinding.inflate(layoutInflater)

    override val viewModel: CalendarInstructorViewModel by viewModels()
    lateinit var adapter: CalendarInstructorAdapter
    private val listOfDates = ArrayList<String>()
    private val listOfTimes = ArrayList<String>()
    private var timesForAdapter = arrayListOf(
        Times("08:00", false),
        Times("09:00", false),
        Times("10:00", false),
        Times("11:00", false),
        Times("13:00", false),
        Times("14:00", false),
        Times("15:00", false),
        Times("16:00", false),
        Times("17:00", false),
        Times("18:00", false)
    )

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(LIST_OF_DATES, listOfDates)
        outState.putStringArrayList(LIST_OF_TIMES, listOfTimes)
        outState.putBundle(ADAPTER_STATE, adapter.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            listOfTimes.addAll(savedInstanceState.getStringArrayList(LIST_OF_TIMES) ?: emptyList())
            adapter.onRestoreInstanceState(savedInstanceState.getBundle(ADAPTER_STATE) ?: Bundle())
        }
    }

    override fun initialize() {
        super.initialize()
        with(binding) {
            val today = CalendarDay.today()
            calendarView.setDateSelected(today, false)
            calendarView.selectionMode = SELECTION_MODE_MULTIPLE
            calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getStringArray(R.array.mcv_monthLabels)))
            val customWeekDayFormatter = CalendarWeekDayFormatter()
            calendarView.setWeekDayFormatter(customWeekDayFormatter)
            calendarView.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)
            cantGoBackMonth()
            setGrayDaysDecorator(calendarView)
            adapter = CalendarInstructorAdapter(requireContext(), timesForAdapter)
            timeRecycler.adapter = adapter
            timeRecycler.layoutManager = GridLayoutManager(requireContext(), 4)
            calendarView.setDateSelected(today, false)
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            btnCheckTimetable.setOnClickListener {
                listOfTimes.clear()
                listOfTimes.addAll(adapter.getTimeArray())
                val bundle = Bundle()
                bundle.putStringArrayList(CTFEFARRAYDATES, listOfDates)
                bundle.putStringArrayList(CTFEFARRAYTIMES, listOfTimes)
                findNavController().navigate(R.id.checkTimetableFragment, bundle)
            }
            calendarView.setOnDateChangedListener { _, date, selected ->
                val selectedDate = date.date
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateString = outputDateFormat.format(selectedDate)

                if (selected) {
                    if (!listOfDates.contains(outputDateString)) {
                        listOfDates.add(outputDateString)
                    }
                } else {
                    listOfDates.remove(outputDateString)
                }
            }
        }
    }

    private fun setGrayDaysDecorator(calendarView: MaterialCalendarView) {
        val arguments = arguments
        val nextWeekStart = getNextWeekStart()
        val currentWeekStart = getCurrentWeekStart()
        val nextWeekEnd = getNextWeekEnd()
            val grayDaysDecorator = object : DayViewDecorator {
                override fun shouldDecorate(day: CalendarDay): Boolean {
                    return !((day.isInRange(nextWeekStart, nextWeekEnd)))
                }

                override fun decorate(view: DayViewFacade) {
                    view.addSpan(
                        ForegroundColorSpan(
                            ContextCompat.getColor(requireContext(), R.color.gray)
                        )
                    )
                    view.setDaysDisabled(true)
                }
            }

            calendarView.addDecorator(grayDaysDecorator)
    }

    private fun getCurrentWeekStart(): CalendarDay {
        val today = Calendar.getInstance()
        return CalendarDay.from(today)
    }

    private fun getNextWeekStart(): CalendarDay {
        val today = Calendar.getInstance()
        today.add(Calendar.WEEK_OF_YEAR, 1)
        today.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return CalendarDay.from(today)
    }

    private fun getNextWeekEnd(): CalendarDay {
        val today = Calendar.getInstance()
        today.add(Calendar.WEEK_OF_YEAR, 1)
        today.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        return CalendarDay.from(today)
    }

    private fun cantGoBackMonth() {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.DAY_OF_MONTH, 1)
        binding.calendarView.state().edit().setMinimumDate(CalendarDay.from(currentDate)).commit()
        val today = CalendarDay.today()
        binding.calendarView.setDateSelected(today, true)
    }
}