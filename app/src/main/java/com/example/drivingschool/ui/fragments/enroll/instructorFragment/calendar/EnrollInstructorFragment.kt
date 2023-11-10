package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentEnrollInstructorBinding
import com.example.drivingschool.tools.timePressed
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter.CalendarAdapter
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class EnrollInstructorFragment :
    BaseFragment<FragmentEnrollInstructorBinding, EnrollInstructorViewModel>(R.layout.fragment_enroll_instructor),
    CalendarAdapter.OnItemListener {

    override val binding by viewBinding(FragmentEnrollInstructorBinding::bind)
    override val viewModel: EnrollInstructorViewModel by viewModels()

    lateinit var calendarRecyclerView: RecyclerView
    lateinit var monthYearText: TextView
    lateinit var selectedDate: LocalDate
    lateinit var adapter: CalendarAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enroll_instructor, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //RequiresApi = setMonthView()
        setMonthView()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun initialize() {
        super.initialize()
        calendarRecyclerView = binding.calendarRecycler
        monthYearText = binding.tvMonthYear
        //RequiresApi = .now()
        selectedDate = LocalDate.now()

        with(binding){
            time8.tvTime.text = getString(R.string.time_8_00)
            time9.tvTime.text = getString(R.string.time_9_00)
            time10.tvTime.text = getString(R.string.time_10_00)
            time11.tvTime.text = getString(R.string.time_11_00)
            time12.tvTime.text = getString(R.string.time_12_00)
            time13.tvTime.text = getString(R.string.time_13_00)
            time14.tvTime.text = getString(R.string.time_14_00)
            time15.tvTime.text = getString(R.string.time_15_00)
            time16.tvTime.text = getString(R.string.time_16_00)
            time17.tvTime.text = getString(R.string.time_17_00)
            time18.tvTime.text = getString(R.string.time_18_00)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupListeners() {
        super.setupListeners()
        with(binding) {
            btnPreviousMonth.setOnClickListener {
                //RequiresApi = previousMonthAction(), nextMonthAction()
                previousMonthAction()
            }
            btnNextMonth.setOnClickListener {
                nextMonthAction()
            }
            btnCheckTimetable.setOnClickListener {
                findNavController().navigate(R.id.checkTimetableFragment)
            }
            time8.tvTime.timePressed()
            time9.tvTime.timePressed()
            time10.tvTime.timePressed()
            time11.tvTime.timePressed()
            time12.tvTime.timePressed()
            time13.tvTime.timePressed()
            time14.tvTime.timePressed()
            time15.tvTime.timePressed()
            time16.tvTime.timePressed()
            time17.tvTime.timePressed()
            time18.tvTime.timePressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        //RequiresApi = monthYearFromDate()

        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth: ArrayList<String> = daysInMonthArray(selectedDate)
        //Здесь делаем смещение на 1 день, чтобы показало корректно с изменением на понедельник
        daysInMonth.removeAt(0)

        adapter = CalendarAdapter(daysInMonth, this)
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)

        val currentWeekNumber = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

        for (i in 0 until adapter.itemCount) {
            val day = daysInMonth[i]
            if (day.isNotEmpty()) {
                val dayInt = day.toInt()
                val weekNumber = selectedDate.withDayOfMonth(dayInt)
                    .get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
                if (weekNumber == currentWeekNumber) {
                    adapter.setItemColor(i, R.color.black)
                } else {
                    adapter.setItemColor(i,R.color.gray)
                }
            }
        }
        calendarRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String {
        //RequiresApi = ofPattern()
//        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("ru"))
        val month = date.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
        return "$month ${date.year}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formattedDate(date: LocalDate): String {
        //RequiresApi = .now()
        val currentDate = LocalDate.now()
        return monthYearFromDate(currentDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthList: ArrayList<String> = arrayListOf()
        //RequiresApi = .from()
        val yearMonth = YearMonth.from(date)
        val daysInMonthLength = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonthLength + dayOfWeek) {
                daysInMonthList.add("")
            } else {
                daysInMonthList.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthList

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun previousMonthAction() {
        //RequiresApi = minusMonths()
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextMonthAction() {
        //RequiresApi = plusMonths()
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, itemText: String) {
        if (itemText.equals("")) {
            //ReqiresApi = monthYearFromDate()
            val message = "Selected date $itemText ${formattedDate(selectedDate)}"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}