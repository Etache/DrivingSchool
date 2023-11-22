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
import com.example.drivingschool.ui.fragments.Constants
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
    BaseFragment<FragmentSelectDateTimeBinding, EnrollViewModel>(R.layout.fragment_select_date_time) {

    override val binding by viewBinding(FragmentSelectDateTimeBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    private val adapter = TimeAdapter(this::onTimeClick)
    private lateinit var workWindows: ArrayList<Date>
    private lateinit var instructorFullName: String
    private var instructorID: String? = null
    private var selectedFinalDate: String? = null
    private var selectedFinalTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_date_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workWindows = arguments?.getSerializable(Constants.WORK_WINDOWS) as ArrayList<Date>
        instructorFullName = arguments?.getString(Constants.FULL_NAME).toString()
        instructorID = arguments?.getString(Constants.INSTRUCTOR_ID_ENROLL).toString()
        Log.d(
            "madimadi",
            "dates in SelectDateTimeFragment: $workWindows $instructorFullName $instructorID"
        )
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
            calendarView.setDateSelected(today, false);
        }
    }

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
            }

        }
        binding.calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
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

                selectedFinalDate = outputDateString //saveSelectedDate
                 {
                    Log.d("madimadi", "onDateSelected: contains: $outputDateString")
                }
                workWindows.forEach { dates ->
                    if (dates.date == outputDateString) {
                        //val timesList: ArrayList<TimeInWorkWindows> = arrayListOf()
//                            val inputTime = LocalTime.parse(times.time, DateTimeFormatter.ofPattern("HH:mm:ss"))
//                            val convertedTime = inputTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//                            var selectedTime = TimeInWorkWindows(convertedTime, times.isFree)
                            //timesList.add(TimeInWorkWindows(times.time, times.isFree))
                        Log.d("madimadi", "convertedTime: ${dates.times}")
                        adapter.setTimesList(dates.times)
                    } else {
                        adapter.setTimesList(
                            arrayListOf(
                                TimeInWorkWindows("8:00", false),
                                TimeInWorkWindows("9:00", false),
                                TimeInWorkWindows("10:00", false),
                                TimeInWorkWindows("11:00", false),
                                TimeInWorkWindows("13:00", false),
                                TimeInWorkWindows("14:00", false),
                                TimeInWorkWindows("15:00", false),
                                TimeInWorkWindows("16:00", false),
                                TimeInWorkWindows("17:00", false),
                                TimeInWorkWindows("18:00", false)
                            )
                        )
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onTimeClick(time: TimeInWorkWindows) {
        val oldTime = LocalTime.parse(time.time, DateTimeFormatter.ofPattern("HH:mm"))
        selectedFinalTime = oldTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

    private fun setGrayDaysDecorator(calendarView: MaterialCalendarView) {
        val nextWeekStart = getCurrentWeekStart()
        val nextWeekEnd = getCurrentWeekEnd()

        val grayDaysDecorator = object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                return !((day.isAfter(nextWeekStart) || day == nextWeekStart) && (day.isBefore(
                    nextWeekEnd
                ) || day == nextWeekEnd))
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

    private fun getCurrentWeekStart(): CalendarDay {
        val today = Calendar.getInstance()
        today.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return CalendarDay.from(today)
    }

    private fun getCurrentWeekEnd(): CalendarDay {
        val today = Calendar.getInstance()
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