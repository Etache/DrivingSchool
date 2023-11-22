package com.example.drivingschool.ui.fragments.enroll.selectDate.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.R
import com.example.drivingschool.data.models.Time
import com.example.drivingschool.data.models.TimeInWorkWindows
import com.example.drivingschool.databinding.ItemTimeBinding
import com.example.drivingschool.tools.timePressed

class TimeAdapter(val onClick : (TimeInWorkWindows) -> Unit) : Adapter<TimeAdapter.TimeViewHolder>() {

    var list = arrayListOf<TimeInWorkWindows>(
        TimeInWorkWindows("8:00", false),
        TimeInWorkWindows("9:00", false),
        TimeInWorkWindows("10:00", false),
        TimeInWorkWindows("11:00", false),
        TimeInWorkWindows("13:00", false),
        TimeInWorkWindows("14:00", false),
        TimeInWorkWindows("15:00", false),
        TimeInWorkWindows("16:00", false),
        TimeInWorkWindows("17:00", false),
        TimeInWorkWindows("18:00", false),
    )

    @SuppressLint("NotifyDataSetChanged")
    fun setTimesList(timesList: ArrayList<TimeInWorkWindows>) {
        list.clear()
        list = timesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder =
        TimeViewHolder(ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    inner class TimeViewHolder(val binding: ItemTimeBinding) : ViewHolder(binding.root) {
        fun onBind(time: TimeInWorkWindows) {
            binding.tvText.text = time.time

            if (time.isFree == true) {
                binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector_normal)
                binding.tvText.setTextColor(Color.parseColor("#8E8E8E"))
            } else {
                binding.tvText.isClickable = false
                binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector_inactive)
                binding.tvText.setTextColor(Color.parseColor("#D3D3D3"))
            }

            var isPressed = false
            binding.tvText.setOnClickListener {
                onClick(time)
                if(time.isFree == true){
                    isPressed = !isPressed
                    if (isPressed) {
                        binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector)
                        binding.tvText.setTextColor(Color.parseColor("#5883CB"))
                    } else {
                        binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector_normal)
                        binding.tvText.setTextColor(Color.parseColor("#8E8E8E"))
                    }
                }
            }
        }
    }
}