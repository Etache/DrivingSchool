package com.example.drivingschool.ui.fragments.enroll.student.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.R
import com.example.drivingschool.data.models.TimeInWorkWindows
import com.example.drivingschool.databinding.ItemTimeBinding

class TimeAdapter(val onClick: (TimeInWorkWindows?) -> Unit) :
    Adapter<TimeAdapter.TimeViewHolder>() {

    var list = listOf<TimeInWorkWindows>()
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    @SuppressLint("NotifyDataSetChanged")
    fun setTimesList(timesList: List<TimeInWorkWindows>?) {
        if (timesList != null) {
            list = timesList
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder =
        TimeViewHolder(ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = list[position]
        holder.onBind(time, position == selectedPosition)
    }

    inner class TimeViewHolder(val binding: ItemTimeBinding) : ViewHolder(binding.root) {
        fun onBind(time: TimeInWorkWindows, isSelected: Boolean) {
            binding.tvText.text = time.time

            if (time.isFree == true) {
                if (isSelected) {
                    binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector)
                    binding.tvText.setTextColor(Color.parseColor("#5883CB"))
                } else {
                    binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector_normal)
                    binding.tvText.setTextColor(Color.parseColor("#8E8E8E"))
                }
            } else {
                binding.tvText.isClickable = false
                binding.tvText.setBackgroundResource(R.drawable.calendar_time_selector_inactive)
                binding.tvText.setTextColor(Color.parseColor("#D3D3D3"))
            }

            itemView.setOnClickListener {
                if (time.isFree == true) {
                    handleTimeClick(adapterPosition)
                    onClick(time)
                } else {
                    onClick(null)
                }
            }
        }

    }

    private fun handleTimeClick(position: Int) {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            // Сброс цвета для предыдущего выбранного времени
            notifyItemChanged(selectedPosition)
        }

        // Обновление цвета для нового выбранного времени
        selectedPosition = position
        notifyItemChanged(selectedPosition)
    }
}