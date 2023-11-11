package com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.databinding.ItemCheckTimetableDateAndTimeBinding

class CheckTimetableAdapter :
    RecyclerView.Adapter<CheckTimetableAdapter.CheckTimetableViewHolder>() {
    private val dateAndTime = arrayListOf(
        "13.12.2023, 08:00, 09:00, 10:00, 11:00, 12:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "14.12.2023, 08:00, 09:00, 10:00, 11:00, 12:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "15.12.2023, 08:00, 09:00, 10:00, 11:00, 12:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "16.12.2023, 08:00, 09:00, 10:00, 11:00, 12:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "17.12.2023, 08:00, 09:00, 10:00, 11:00, 12:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "18.12.2023, 08:00, 09:00, 10:00, 11:00, 12:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
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
            //binding.tvDateAndTime.text = dateAndTime[bindingAdapterPosition]
        }
    }
}