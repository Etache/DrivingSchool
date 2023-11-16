package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.databinding.ItemEnrollInstructorTimeBinding
import com.example.drivingschool.tools.timePressed
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter.EnrollInstructorAdapter.*

class EnrollInstructorAdapter : RecyclerView.Adapter<TimeViewHolder>() {

    private val timeArrayList = arrayListOf(
        "08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(
            ItemEnrollInstructorTimeBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = timeArrayList.size

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.onBind(timeArrayList[position])
    }

    inner class TimeViewHolder(private val binding: ItemEnrollInstructorTimeBinding) :
        ViewHolder(binding.root) {

        fun onBind(text: String) {
//            binding.tvTime.text = timeArrayList[bindingAdapterPosition]
            binding.tvTime.timePressed()
        }
    }
}