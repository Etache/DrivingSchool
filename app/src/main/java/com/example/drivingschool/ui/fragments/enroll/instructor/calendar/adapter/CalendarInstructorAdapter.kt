package com.example.drivingschool.ui.fragments.enroll.instructor.calendar.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.Times
import com.example.drivingschool.databinding.ItemEnrollInstructorTimeBinding
import com.example.drivingschool.ui.fragments.Constants.CTFEFARRAYTIMES

class CalendarInstructorAdapter(
    private val context: Context,
    private var times: ArrayList<Times>
) : RecyclerView.Adapter<CalendarInstructorAdapter.TimeViewHolder>() {

    private val timesArray = arrayListOf<String>()

    fun getTimeArray(): ArrayList<String> {
        for (it in times) {
            if (it.pressed == true && !timesArray.contains(it.time)) {
                it.time?.let { timesArray.add(it) }
            } else if (it.pressed == false && timesArray.contains(it.time)) {
                timesArray.remove(it.time)
            }
        }
        return timesArray
    }

    fun onSaveInstanceState(): Bundle {
        val bundle = Bundle()
        bundle.putStringArrayList(CTFEFARRAYTIMES, timesArray)
        return bundle
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        timesArray.clear()
        timesArray.addAll(bundle.getStringArrayList(CTFEFARRAYTIMES) ?: emptyList())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(
            ItemEnrollInstructorTimeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = times.size

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.onBind(times[position])
    }

    inner class TimeViewHolder(private val binding: ItemEnrollInstructorTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(times: Times) {
            with(binding) {
                binding.tvTime.text = times.time
                val normalBackground = R.drawable.calendar_time_selector_normal
                val pressedBackground = R.drawable.calendar_time_selector

                if (times.pressed == true) {
                    tvTime.setBackgroundResource(pressedBackground)
                    tvTime.setTextColor(ContextCompat.getColor(context, R.color.light_blue))
                } else {
                    tvTime.setBackgroundResource(normalBackground)
                    tvTime.setTextColor(ContextCompat.getColor(context, R.color.gray))
                }

                tvTime.setOnClickListener {
                    if (times.pressed == false) {
                        tvTime.setBackgroundResource(pressedBackground)
                        tvTime.setTextColor(ContextCompat.getColor(context, R.color.light_blue))
                        timesArray.add(tvTime.text.toString())
                        times.pressed = true
                    } else {
                        tvTime.setBackgroundResource(normalBackground)
                        tvTime.setTextColor(ContextCompat.getColor(context, R.color.gray))
                        timesArray.remove(tvTime.text.toString())
                        times.pressed = false
                    }
                }
            }
        }
    }
}