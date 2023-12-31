package com.example.drivingschool.ui.fragments.studentMain.lesson

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
import java.text.SimpleDateFormat
import java.util.Locale

class LessonAdapter(
    private val onClick: (String) -> Unit,
    private val context: Context,
    private val lessonType: LessonType?
) :
    RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

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
                val last = lesson.instructor?.lastname ?: ""
                tvTitle.text = context.getString(R.string.person_full_name,
                    lesson.instructor?.surname,
                    lesson.instructor?.name,
                    last)
                tvDate.text = lesson.date?.let { formatDate(it) }
                tvTime.text = timeWithoutSeconds(lesson.time)
                tvStatus.text = getStatus(lesson.status, binding.tvStatus, context)
            }
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

        private fun formatDate(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(inputDate) ?: return ""

            val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
            return outputFormat.format(date).replaceFirstChar { it.uppercase() }
        }
    }
}
