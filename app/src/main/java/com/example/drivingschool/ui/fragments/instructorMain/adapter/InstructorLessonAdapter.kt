package com.example.drivingschool.ui.fragments.instructorMain.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.databinding.InstructorMainItemBinding


class InstructorLessonAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<InstructorLessonAdapter.LessonViewHolder>() {

    private val lessons = arrayListOf<LessonsItem>()

    inner class LessonViewHolder(private val binding: InstructorMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(lesson: LessonsItem) {
            binding.tvTitle.text = "${lesson.student?.surname} ${lesson.student?.name} ${lesson.student?.lastname}"
            binding.tvDate.text = lesson.date
            binding.tvTime.text = lesson.time

            itemView.setOnClickListener {
                onClick(lesson.id.toString())
            }
            Log.d("ahahaha", "установлены данные на странице инструктора")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(
            InstructorMainItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

    override fun getItemCount() =
        lessons.size
}