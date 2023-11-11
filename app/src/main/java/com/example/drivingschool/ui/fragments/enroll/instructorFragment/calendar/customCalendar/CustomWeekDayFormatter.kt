package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.customCalendar

import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CustomWeekDayFormatter : WeekDayFormatter {
    override fun format(weekday: Int): CharSequence? {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_WEEK] = weekday
        val simpleDateFormat = SimpleDateFormat("E", Locale.getDefault())
        return simpleDateFormat.format(calendar.time).substring(0, 1)
            .uppercase(Locale.getDefault())
    }
}