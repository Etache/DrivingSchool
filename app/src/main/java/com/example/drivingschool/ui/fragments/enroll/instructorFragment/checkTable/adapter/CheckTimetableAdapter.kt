package com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.databinding.ItemCheckTimetableDateAndTimeBinding

class CheckTimetableAdapter :
    RecyclerView.Adapter<CheckTimetableAdapter.CheckTimetableViewHolder>() {
    private val dateAndTime = arrayListOf(
        "12.12.2023, 08:00, 09:00,10:00",
        "12.12.2023, 08:00, 09:00,10:00",
        "12.12.2023, 08:00, 09:00,10:00",
        "12.12.2023, 08:00, 09:00,10:00",
        "12.12.2023, 08:00, 09:00,10:00",
        "12.12.2023, 08:00, 09:00,10:00",
        "12.12.2023, 08:00, 09:00,10:00",
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckTimetableViewHolder {
        return CheckTimetableViewHolder(
            ItemCheckTimetableDateAndTimeBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dateAndTime.size

    override fun onBindViewHolder(holder: CheckTimetableViewHolder, position: Int) {
        holder.onBind(dateAndTime[position])
    }

    inner class CheckTimetableViewHolder(private val binding: ItemCheckTimetableDateAndTimeBinding) :
        ViewHolder(binding.root) {

        fun onBind(itemText: String) {
            //логика по получению данных от бека и засетивание в recyclerView(для этого нужна модель,
            // ока не сделал)
            binding.tvDateAndTime.text = dateAndTime[bindingAdapterPosition]
        }
    }
}