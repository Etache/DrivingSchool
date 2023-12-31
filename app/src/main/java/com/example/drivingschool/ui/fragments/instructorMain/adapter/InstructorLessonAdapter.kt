package com.example.drivingschool.ui.fragments.instructorMain.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.databinding.ItemMainBinding
import com.example.drivingschool.ui.fragments.studentMain.lesson.LessonStatus
import com.example.drivingschool.ui.fragments.studentMain.lesson.LessonType
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
                tvTitle.text = context.getString(R.string.person_full_name,
                    lesson.student?.surname,
                    lesson.student?.name)
                tvTime.text = timeWithoutSeconds(lesson.time)
                tvStatus.text = getStatus(lesson.status, binding.tvStatus, context)
                tvDate.text = lesson.date?.let { formatDate(it) }
            }
            Log.e("ololo", "bind: $lessonType")
            Log.d("ololo", "bind: position = $position")
            if (position == 0 && lessonType == LessonType.Current) {
                binding.apply {
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.dark_blue))
                    dividerView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.dark_blue
                        )
                    )
                }
            }

            itemView.setOnClickListener {
                if (lesson.status != LessonStatus.ABSENT.status && lesson.status != LessonStatus.CANCELED.status) {
                    onClick(lesson.id.toString())
                }
            }
        }

        private fun formatDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(inputDate) ?: return ""

            val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
            return outputFormat.format(date).replaceFirstChar { it.uppercase() }
        }

        private fun getStatus(status: String?, tvStatus: TextView, context: Context): String {
            return when (status) {
                LessonStatus.PLANNED.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.bright_blue))
                    context.getString(R.string.planned)
                }

                LessonStatus.ACTIVE.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
                    context.getString(R.string.active_ru)
                }

                LessonStatus.CANCELED.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
                    context.getString(R.string.canceled)
                }

                LessonStatus.FINISHED.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_gray_text))
                    context.getString(R.string.finished)
                }

                LessonStatus.ABSENT.status -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
                    context.getString(R.string.absent)
                }

                else -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_gray_text))
                    context.getString(R.string.unknown_status)
                }
            }
        }

        private fun timeWithoutSeconds(inputTime: String?): String {
            val timeParts = inputTime?.split(":")
            return "${timeParts?.get(0)}:${timeParts?.get(1)}"
        }
    }
}
