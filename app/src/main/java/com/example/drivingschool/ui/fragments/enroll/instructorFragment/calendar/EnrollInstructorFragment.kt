package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar

import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentEnrollInstructorBinding
import com.example.drivingschool.tools.timePressed
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.customCalendar.CustomWeekDayFormatter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            time8.tvTime.text = getString(R.string.time_8_00)
            time9.tvTime.text = getString(R.string.time_9_00)
            time10.tvTime.text = getString(R.string.time_10_00)
            time11.tvTime.text = getString(R.string.time_11_00)
            time13.tvTime.text = getString(R.string.time_13_00)
            time14.tvTime.text = getString(R.string.time_14_00)
            time15.tvTime.text = getString(R.string.time_15_00)
            time16.tvTime.text = getString(R.string.time_16_00)
            time17.tvTime.text = getString(R.string.time_17_00)
            time18.tvTime.text = getString(R.string.time_18_00)

            //сделано: выбор нескольких дат, закрытие отображение дней из других месяцев
            //цвет выбора дней, изменил отображение месяца(теперь с большой буквы), названия недель,
            //теперь тоже с большой буквы, и один символ + изменен цвет на серый
            //ограничить переход на предыдущий месяц из настоящего месяца,изменить отображение других недель
            //Добавил прокрутку экрана в случае если все не помещается на экран
            calendarView.selectionMode = SELECTION_MODE_MULTIPLE
            calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getStringArray(R.array.mcv_monthLabels)))
            val customWeekDayFormatter = CustomWeekDayFormatter()
            calendarView.setWeekDayFormatter(customWeekDayFormatter)
            calendarView.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)
            //чтобы возможность выбрать была только на следующую неделю в пятницу или субботу
            cantGoBackMonth()
            setGrayDaysDecorator(calendarView)
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
                view.addSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), android.R.color.darker_gray)))
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

    private fun cantGoBackMonth(){
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.DAY_OF_MONTH, 1)
        binding.calendarView.state().edit().setMinimumDate(CalendarDay.from(currentDate)).commit()

        val today = CalendarDay.today()
        binding.calendarView.setDateSelected(today, true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            btnCheckTimetable.setOnClickListener {
                findNavController().navigate(R.id.checkTimetableFragment)
            }
            time8.tvTime.timePressed()
            time9.tvTime.timePressed()
            time10.tvTime.timePressed()
            time11.tvTime.timePressed()
            time13.tvTime.timePressed()
            time14.tvTime.timePressed()
            time15.tvTime.timePressed()
            time16.tvTime.timePressed()
            time17.tvTime.timePressed()
            time18.tvTime.timePressed()
        }
    }
}