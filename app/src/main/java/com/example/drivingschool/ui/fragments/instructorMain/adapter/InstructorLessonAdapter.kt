package com.example.drivingschool.ui.fragments.instructorMain.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.databinding.ItemMainBinding
import com.example.drivingschool.ui.fragments.main.lesson.LessonStatus
import com.example.drivingschool.ui.fragments.main.lesson.LessonType
import java.text.SimpleDateFormat
import java.util.Locale

class InstructorLessonAdapter(
    private val onClick: (String) -> Unit,
    private val context: Context,
    private val lessonType: LessonType?
) :
    RecyclerView.Adapter<InstructorLessonAdapter.ViewHolder>() {

    private var lessons = arrayListOf<LessonsItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(it: List<LessonsItem>) {
        lessons = it as ArrayList<LessonsItem>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = lessons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lessons[position], position)
    }

    inner class ViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: LessonsItem, position: Int) {
            binding.apply {
                val last = lesson.student?.lastname ?: ""
                tvTitle.text = "${lesson.student?.surname} ${lesson.student?.name} $last"
                tvTime.text = timeWithoutSeconds(lesson.time)
                tvStatus.text = getStatus(lesson.status, binding.tvStatus, context)
            }
            if (position == 0 && lessonType == LessonType.Current) {
                binding.apply {
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
                    dividerView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                }

                val originalDate = lesson.date
                val parts = (originalDate?.split("-"))!!
                val day = parts[2].toInt()
                val month = parts[1].toInt()
                val monthString = when (month) {
                    1 -> "января"
                    2 -> "февраля"
                    3 -> "марта"
                    4 -> "апреля"
                    5 -> "мая"
                    6 -> "июня"
                    7 -> "июля"
                    8 -> "августа"
                    9 -> "сентября"
                    10 -> "октября"
                    11 -> "ноября"
                    12 -> "декабря"
                    else -> throw IllegalArgumentException("Некорректный месяц: $month")
                }
                binding.tvDate.text = "$day $monthString"
            }

            itemView.setOnClickListener {
                onClick(lesson.id.toString())
            }
        }

        private fun getStatus(status: String?, tvStatus: TextView, context: Context): String {
            return when (status) {
                LessonStatus.PLANNED.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.bright_blue))
                    "Запланирован"
                }

                LessonStatus.ACTIVE.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
                    "Активен"
                }

                LessonStatus.CANCELED.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
                    "Отменен"
                }

                LessonStatus.FINISHED.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_gray_text))
                    "Завершен"
                }

                else -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_gray_text))
                    "Неизвестный статус"
                }
            }
        }

        private fun timeWithoutSeconds(inputTime: String?): String {
            val timeParts = inputTime?.split(":")
            return "${timeParts?.get(0)}:${timeParts?.get(1)}"
        }
    }
}
