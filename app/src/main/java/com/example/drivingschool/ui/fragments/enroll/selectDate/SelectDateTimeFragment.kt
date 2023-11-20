package com.example.drivingschool.ui.fragments.enroll.selectDate

import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.Date
import com.example.drivingschool.data.models.TimeInWorkWindows
import com.example.drivingschool.databinding.FragmentSelectDateTimeBinding
import com.example.drivingschool.ui.fragments.BundleKeys
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.customCalendar.CalendarWeekDayFormatter
import com.example.drivingschool.ui.fragments.enroll.selectDate.adapter.TimeAdapter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class SelectDateTimeFragment :
    BaseFragment<FragmentSelectDateTimeBinding, EnrollViewModel>(R.layout.fragment_select_date_time){

    override val binding by viewBinding(FragmentSelectDateTimeBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()
    private val adapter = TimeAdapter(this::onTimeClick)
    private lateinit var workWindows : ArrayList<Date>
    private lateinit var instructorFullName : String
    private lateinit var selectedFinalDate : String
    private lateinit var selectedFinalTime : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_date_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workWindows = arguments?.getSerializable(BundleKeys.WORK_WINDOWS) as ArrayList<Date>
        instructorFullName = arguments?.getString(BundleKeys.FULL_NAME).toString()
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
            val customWeekDayFormatter = CalendarWeekDayFormatter()
            calendarView.setWeekDayFormatter(customWeekDayFormatter)
            calendarView.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)
            cantGoBackMonth()
            //setGrayDaysDecorator(calendarView)
            calendarView.setDateSelected(today, false);
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.btnConfirm.setOnClickListener {
            if(selectedFinalDate != null && selectedFinalTime != null) {
                val bundle = Bundle()
                bundle.putString(BundleKeys.TIMETABLE_TO_ENROLL_DATE, selectedFinalDate)
                bundle.putString(BundleKeys.TIMETABLE_TO_ENROLL_TIME, selectedFinalTime)
                bundle.putString(BundleKeys.FULL_NAME, instructorFullName)
                findNavController().navigate(R.id.enrollFragment, bundle)
            }

        }
        binding.calendarView.setOnDateChangedListener(object : OnDateSelectedListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
//                Log.d("madimadi", "date selected: ${binding.calendarView.selectedDate.date.date}")
//                Log.d("madimadi", "date selected: ${binding.calendarView.selectedDate.date}")
//                Log.d("madimadi", "date selected: ${binding.calendarView.selectedDate}")
                var selectedDate = binding.calendarView.selectedDate.date.toString()
                val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
                val date = inputDateFormat.parse(selectedDate)
                val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val outputDateString = outputDateFormat.format(date)

                selectedFinalDate = outputDateString                         //saveSelectedDate
                workWindows.forEach{ dates ->
                    if(dates.date == outputDateString) {
                        val timesList : ArrayList<TimeInWorkWindows> = arrayListOf()
                        dates.times.forEach{ times ->
                            val inputTime = LocalTime.parse(times.time, DateTimeFormatter.ofPattern("HH:mm:ss"))
                            val convertedTime = inputTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                            var selectedTime = TimeInWorkWindows(convertedTime, times.isFree)
                            timesList.add(selectedTime)
                            Log.d("madimadi", "convertedTime: $selectedTime")
                        }
//                        timesList.forEach{time ->
//                            val inputTime = LocalTime.parse(dates.times, DateTimeFormatter.ofPattern("HH:mm:ss"))
//                            val convertedTime = inputTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//                            time.time = convertedTime
//                            Log.d("madimadi", "convertedTime: ${time.time}")
//                        }
                        //adapter.setTimesList(timesList)
                    }
                }
            }
        })
    }

    fun onTimeClick(time : TimeInWorkWindows){
        selectedFinalTime = time.time.toString()
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