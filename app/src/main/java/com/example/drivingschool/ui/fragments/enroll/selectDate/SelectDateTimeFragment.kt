package com.example.drivingschool.ui.fragments.enroll.selectDate

import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.Date
import com.example.drivingschool.databinding.FragmentSelectDateTimeBinding
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter.EnrollInstructorAdapter
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.customCalendar.EnrollWeekDayFormatter
import com.example.drivingschool.ui.fragments.enroll.selectDate.adapter.TimeAdapter
import com.example.drivingschool.ui.fragments.enroll.selectInstructor.SelectInstructorFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import java.util.Calendar


class SelectDateTimeFragment :
    BaseFragment<FragmentSelectDateTimeBinding, EnrollViewModel>(R.layout.fragment_select_date_time){

    override val binding by viewBinding(FragmentSelectDateTimeBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()
    private val adapter = TimeAdapter()
    private lateinit var workWindows : ArrayList<Date>
    private var dates: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_date_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workWindows = arguments?.getSerializable(SelectInstructorFragment.WORK_WINDOWS) as ArrayList<Date>
        Log.d("madimadi", "dates in SelectDateTimeFragment: $workWindows")
    }

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
            val customWeekDayFormatter = EnrollWeekDayFormatter()
            calendarView.setWeekDayFormatter(customWeekDayFormatter)
            calendarView.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)
            cantGoBackMonth()
            setGrayDaysDecorator(calendarView)
            calendarView.setDateSelected(today, false);
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.btnConfirm.setOnClickListener {
            findNavController().navigate(R.id.enrollFragment)
        }
    }

    private fun setGrayDaysDecorator(calendarView: MaterialCalendarView) {
        val nextWeekStart = getNextWeekStart()
        val nextWeekEnd = getNextWeekEnd()

        val grayDaysDecorator = object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return !((day.isAfter(nextWeekStart) || day == nextWeekStart) && (day.isBefore(nextWeekEnd) || day == nextWeekEnd))
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