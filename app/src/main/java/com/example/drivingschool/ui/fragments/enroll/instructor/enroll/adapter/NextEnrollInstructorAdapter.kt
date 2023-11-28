package com.example.drivingschool.ui.fragments.enroll.instructor.enroll.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.InstructorWorkWindowResponse
import com.example.drivingschool.databinding.ItemCheckTimetableDateAndTimeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NextEnrollInstructorAdapter(
    private val nextWeek: List<InstructorWorkWindowResponse.NextWeek>?
) :
    RecyclerView.Adapter<NextEnrollInstructorAdapter.EnrollInstructorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrollInstructorViewHolder {
        return EnrollInstructorViewHolder(
            ItemCheckTimetableDateAndTimeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return nextWeek?.size!!
    }

    override fun onBindViewHolder(holder: EnrollInstructorViewHolder, position: Int) {
        nextWeek?.get(position)?.let { holder.onBind(it) }
    }

    inner class EnrollInstructorViewHolder(private val binding: ItemCheckTimetableDateAndTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(currentWeek: InstructorWorkWindowResponse.NextWeek) {
            val spannableStringBuilder = SpannableStringBuilder()

            spannableStringBuilder.append(getDayOfWeek(currentWeek.date ?: ""))
            spannableStringBuilder.append(" ")
            spannableStringBuilder.append(changeDateFormat(currentWeek.date ?: ""))
            spannableStringBuilder.append("\n")

            currentWeek.times?.forEach { time ->
                if (time.isFree == false) {
                    val greenText = "${time.time} "
                    val spannableString = SpannableString(greenText)
                    spannableString.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.green)),
                        0,
                        greenText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableStringBuilder.append(spannableString)
                } else {
                    spannableStringBuilder.append("${time.time} ")
                }
            }

            val stringAfterSpannable = SpannableString(spannableStringBuilder)
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
