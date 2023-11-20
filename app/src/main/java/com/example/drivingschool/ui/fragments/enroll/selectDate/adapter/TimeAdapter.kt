package com.example.drivingschool.ui.fragments.enroll.selectDate.adapter

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

class TimeAdapter(val onClick : (TimeInWorkWindows) -> Unit) : Adapter<TimeAdapter.TimeViewHolder>(){

    val list = arrayListOf<Time>(
        Time("8:00", true, false),
        Time("9:00", true, false),
        Time("10:00", true, false),
        Time("11:00", true, false),
        Time("13:00", true, false),
        Time("14:00", true, false),
        Time("15:00", true, false),
        Time("16:00", true, false),
        Time("17:00", true, false),
        Time("18:00", true, false)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder =
        TimeViewHolder(ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    inner class TimeViewHolder(val binding : ItemTimeBinding) : ViewHolder(binding.root) {
        fun onBind(time : Time){
            binding.tvText.text = time.text

            binding.tvText.setOnClickListener{
                time.isSelected = !time.isSelected!!
                Log.d("madimadi", "isSelected : ${time.isSelected}")

                if(time.isSelected == true) {
                    binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector)
                    binding.tvText.setTextColor(Color.parseColor("#5883CB"));
                    Log.d("madimadi", "background : ${binding.tvText.background}")
                } else {
                    binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector_normal)
                    binding.tvText.setTextColor(Color.parseColor("#8E8E8E"));
                    Log.d("madimadi", "background : ${binding.tvText.background}")
                }
            }
        }
    }
}