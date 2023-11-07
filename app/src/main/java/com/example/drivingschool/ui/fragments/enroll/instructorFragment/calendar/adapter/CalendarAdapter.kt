package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.databinding.ItemCalendarBinding

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val daysOfMonth = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {

        return CalendarViewHolder(
            ItemCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = daysOfMonth.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class CalendarViewHolder(private val binding: ItemCalendarBinding) :
        ViewHolder(binding.root) {

        private lateinit var dayOfMonth: TextView
    }
}