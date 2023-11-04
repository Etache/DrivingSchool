package com.example.drivingschool.ui.fragments.main.lesson

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.databinding.ItemMainBinding

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

    inner class ViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: LessonsItem, position: Int) {
            binding.apply {
                tvTitle.text = lesson.instructor?.surname + lesson.instructor?.name
                tvDate.text = lesson.date
                tvTime.text = lesson.time
            }
            if (position == 0 && lessonType == LessonType.Current) {
                binding.apply {
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
                    dividerView.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                }
            }

            itemView.setOnClickListener {
                onClick(lesson.id.toString())
            }

        }

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


}