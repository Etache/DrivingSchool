package com.example.drivingschool.ui.fragments.enroll.instructorFragment.enroll.adapter

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.databinding.ItemCheckTimetableDateAndTimeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EnrollInstructorAdapter(
    val listOfDates: List<String>?,
    val listOfTimes: List<String>?
) :
    RecyclerView.Adapter<EnrollInstructorAdapter.EnrollInstructorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrollInstructorViewHolder {
        return EnrollInstructorViewHolder(
            ItemCheckTimetableDateAndTimeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfDates?.size!!
    }

    override fun onBindViewHolder(holder: EnrollInstructorViewHolder, position: Int) {
        holder.onBind(listOfDates!![position])
    }

    inner class EnrollInstructorViewHolder(private val binding: ItemCheckTimetableDateAndTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(itemText: String) {
            val stringAfterEdit = buildString {
                append(getDayOfWeek(listOfDates!![bindingAdapterPosition]))
                append(" ")
                append(changeDateFormat((listOfDates[bindingAdapterPosition])))
                append("\n")
                append(listOfTimes?.sorted()?.distinct())
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
