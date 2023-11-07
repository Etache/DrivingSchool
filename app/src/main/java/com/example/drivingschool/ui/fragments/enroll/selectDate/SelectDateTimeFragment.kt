package com.example.drivingschool.ui.fragments.enroll.selectDate

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drivingschool.R
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.jaredrummler.materialspinner.MaterialSpinner
import java.util.Calendar

@Suppress("UNREACHABLE_CODE")
class SelectDateTimeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_date_time, container, false)

        calendarCustom()
    }


    private fun calendarCustom() {
        val compactCalendarView = view?.findViewById<CompactCalendarView>(R.id.cv_calendar)
        compactCalendarView?.setFirstDayOfWeek(Calendar.MONDAY)

        val calendar = Calendar.getInstance()
        val event = Event(Color.BLUE, calendar.timeInMillis, "текущая неделя")
        compactCalendarView?.addEvent(event)

        val todayEvent = Event(Color.BLUE, calendar.timeInMillis, "сегодняшняя дата")
        compactCalendarView?.addEvent(todayEvent)

        compactCalendarView?.setShouldDrawDaysHeader(false)
        compactCalendarView?.displayOtherMonthDays(false)


        val spinner = view?.findViewById<MaterialSpinner>(R.id.ms_calendar_spinner)
        val months = arrayOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", " Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", )
        spinner?.setItems(*months)

        spinner?.setOnItemSelectedListener { _, position, _, _ ->
            val selectedMonth = months[position]
            calendar.set(Calendar.MONTH,position)
            compactCalendarView?.setCurrentDate(calendar.time)
        }
    }
}