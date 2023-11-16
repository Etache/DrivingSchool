package com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.databinding.ItemCheckTimetableDateAndTimeBinding
import com.example.drivingschool.ui.fragments.enroll.instructorFragment.checkTable.adapter.CheckTimetableAdapter.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CheckTimetableAdapter : RecyclerView.Adapter<CheckTimetableViewHolder>() {
    private val dateAndTime = arrayListOf(
        "13.11.2023\n08:00, 09:00, 10:00, 11:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "14.11.2023\n08:00, 09:00, 10:00, 11:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "15.11.2023\n08:00, 09:00, 10:00, 11:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "16.11.2023\n08:00, 09:00, 10:00, 11:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "17.11.2023\n08:00, 09:00, 10:00, 11:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
        "18.11.2023\n08:00, 09:00, 10:00, 11:00, 13:00, 14:00, 15:00, 16:00, 17:00, 18:00",
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
            //пока не сделал)
            val stringAfterEdit = buildString {
//                append(getDayOfWeek(dateAndTime[bindingAdapterPosition]))
//                append(" ")
//                append(dateAndTime[bindingAdapterPosition])
            }
            val stringAfterSpannable = SpannableString(stringAfterEdit)
            stringAfterSpannable.setSpan(StyleSpan(Typeface.BOLD), 0,13, 0)
            binding.tvDateAndTime.text = stringAfterSpannable
        }

        private fun getDayOfWeek(dateString: String): String {
            val pattern = "dd.MM.yyyy"
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val daysOfWeek = arrayOf("Вc", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")
            return daysOfWeek[dayOfWeek - 1]
        }
    }
}