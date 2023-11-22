package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar

import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter.CalendarInstructorAdapter
import com.example.drivingschool.data.models.Times
import com.example.drivingschool.databinding.FragmentCalendarInstructorBinding
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.customCalendar.CalendarWeekDayFormatter
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.enroll.EnrollInstructorFragment.Companion.EFCIFCURRENTWEEKEMPTY
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarInstructorFragment :
    BaseFragment<FragmentCalendarInstructorBinding, CalendarInstructorViewModel>(R.layout.fragment_calendar_instructor) {
    //Теперь всё работает корректно

    override val binding by viewBinding(FragmentCalendarInstructorBinding::bind)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_instructor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("listOfDates", listOfDates)
        outState.putStringArrayList("listOfTimes", listOfTimes)
        outState.putBundle("adapterState", adapter.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            listOfTimes.addAll(savedInstanceState.getStringArrayList("listOfTimes") ?: emptyList())
            adapter.onRestoreInstanceState(savedInstanceState.getBundle("adapterState") ?: Bundle())
        }
    }

    override fun initialize() {
        super.initialize()
        with(binding) {
            val arguments = arguments
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
            calendarView.setDateSelected(today, false);
        }
    }

    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            btnCheckTimetable.setOnClickListener {
                listOfTimes.clear()
                listOfTimes.addAll(adapter.getTimeArray())
                val bundle = Bundle()
                bundle.putStringArrayList("dates_array", listOfDates)
                bundle.putStringArrayList("times_array", listOfTimes)
                findNavController().navigate(R.id.checkTimetableFragment, bundle)
            }
            calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
                override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                    val selectedDate = date.date
                    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputDateString = outputDateFormat.format(selectedDate)

                    if (selected) {
                        //Если выбрана
                        if (!listOfDates.contains(outputDateString)) {
                            listOfDates.add(outputDateString)
                        }
                    } else {
                        //Если убрана
                        listOfDates.remove(outputDateString)
                    }
                }
            })
        }
    }

    private fun setGrayDaysDecorator(calendarView: MaterialCalendarView) {
        val arguments = arguments
        val nextWeekStart = getNextWeekStart()
        val currentWeekStart = getCurrentWeekStart()
        val nextWeekEnd = getNextWeekEnd()

        if (arguments?.getBoolean(EFCIFCURRENTWEEKEMPTY) == true){
            val grayDaysDecorator = object : DayViewDecorator {
                override fun shouldDecorate(day: CalendarDay): Boolean {
                    return !(day.isInRange(currentWeekStart, nextWeekEnd))
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
        } else {
            val grayDaysDecorator = object : DayViewDecorator {
                override fun shouldDecorate(day: CalendarDay): Boolean {
                    return !((day.isInRange(nextWeekStart,nextWeekEnd)))
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

    companion object{
        const val CTFEFARRAYDATES = "dates_array"
        const val CTFEFARRAYTIMES = "times_array"
        const val ADAPTERSTATE = "adapterState"
        const val LISTOFTIMES = "listOfTimes"
    }
}