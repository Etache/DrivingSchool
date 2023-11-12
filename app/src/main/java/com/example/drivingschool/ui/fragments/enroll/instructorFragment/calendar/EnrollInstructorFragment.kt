package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar

import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
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
import com.example.drivingschool.databinding.FragmentEnrollInstructorBinding
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter.EnrollInstructorAdapter
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.customCalendar.EnrollWeekDayFormatter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_MULTIPLE
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import java.util.Calendar


class EnrollInstructorFragment :
    BaseFragment<FragmentEnrollInstructorBinding, EnrollInstructorViewModel>(R.layout.fragment_enroll_instructor) {

    override val binding by viewBinding(FragmentEnrollInstructorBinding::bind)
    override val viewModel: EnrollInstructorViewModel by viewModels()
    lateinit var adapter: EnrollInstructorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        if (5 != 5) { код для проверки наличия расписания
//            return inflater.inflate(R.layout.fragment_enroll_instructor, container, false)
//        } else {
//            Toast.makeText(requireContext(), "фрагмент не может быть создан", Toast.LENGTH_SHORT).show()
//            findNavController().popBackStack()
//            return null
//        }
        return inflater.inflate(R.layout.fragment_enroll_instructor, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun initialize() {
        super.initialize()

        with(binding) {
            val today = CalendarDay.today()
            calendarView.setDateSelected(today, false)
            calendarView.selectionMode = SELECTION_MODE_MULTIPLE
            calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getStringArray(R.array.mcv_monthLabels)))
            val customWeekDayFormatter = EnrollWeekDayFormatter()
            calendarView.setWeekDayFormatter(customWeekDayFormatter)
            calendarView.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)
            //чтобы возможность выбрать была только на следующую неделю в пятницу или субботу
            cantGoBackMonth()
            setGrayDaysDecorator(calendarView)
            adapter = EnrollInstructorAdapter()
            timeRecycler.adapter = adapter
            timeRecycler.layoutManager = GridLayoutManager(requireContext(), 4)
            calendarView.setDateSelected(today, false);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            btnCheckTimetable.setOnClickListener {
                findNavController().navigate(R.id.checkTimetableFragment)
            }
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