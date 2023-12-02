package com.example.drivingschool.ui.fragments.enroll.instructor.checkTable.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.databinding.ItemCheckTimetableDateAndTimeBinding
import com.example.drivingschool.ui.fragments.enroll.instructor.checkTable.adapter.CheckTimetableAdapter.*
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class CheckTimetableAdapter(
    private val listOfDates: ArrayList<String>?,
    private val listOfTimes: ArrayList<String>?,
) : RecyclerView.Adapter<CheckTimetableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckTimetableViewHolder {
        return CheckTimetableViewHolder(
            ItemCheckTimetableDateAndTimeBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = listOfDates!!.size

    override fun onBindViewHolder(holder: CheckTimetableViewHolder, position: Int) {
        holder.onBind(listOfDates!![position])
    }

    inner class CheckTimetableViewHolder(private val binding: ItemCheckTimetableDateAndTimeBinding) :
        ViewHolder(binding.root) {

        fun onBind(itemText: String) {
            val stringAfterEdit = buildString {
                append(getDayOfWeek(listOfDates!![bindingAdapterPosition]))
                append(" ")
                append(changeDateFormat((listOfDates[bindingAdapterPosition])))
                append("\n")
                append(listOfTimes?.sorted())
            }.replace("[","").replace("]","")
            val stringAfterSpannable = SpannableString(stringAfterEdit)
            stringAfterSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, 13, 0)
            binding.tvDateAndTime.text = stringAfterSpannable
        }

        private fun changeDateFormat(date: String): String {
            val originalFormat = "yyyy-MM-dd"
            val targetFormat = "dd.MM.yyyy"
            val originalDateFormat = SimpleDateFormat(originalFormat, Locale.getDefault())
            val targetDateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())

            val originalDate = originalDateFormat.parse(date)
            return targetDateFormat.format(originalDate)
        }
    }

    private fun getDayOfWeek(dateString: String): String {
        val pattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysOfWeek = arrayOf("Вc", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")
        return daysOfWeek[dayOfWeek - 1]
    }
}