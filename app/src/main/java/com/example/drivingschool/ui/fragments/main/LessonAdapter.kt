package com.example.drivingschool.ui.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.databinding.ItemMainBinding

class LessonAdapter() : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    // Для проверки
    val list = listOf(
        Lesson("Иванов Иван Иванович", "Практическое вождение", "12.12.23", "9:41"),
        Lesson("Иванов Иван ", "Практическое вождение 2", "01.01.23", "9:41"),
        Lesson("Иванов Иван Иванович", "Практическое вождение", "12.12.23", "9:41"),
        Lesson("Иванов Иван ", "Практическое вождение 2", "01.01.23", "9:41"),
        Lesson("Иванов Иван Иванович", "Практическое вождение", "12.12.23", "9:41"),
        Lesson("Иванов Иван ", "Практическое вождение 2", "01.01.23", "9:41"),
        Lesson("Иванов Иван Иванович", "Практическое вождение", "12.12.23", "9:41"),
        Lesson("Иванов Иван ", "Практическое вождение 2", "01.01.23", "9:41"),
        Lesson("Иванов Иван Иванович", "Практическое вождение", "12.12.23", "9:41"),
        Lesson("Иванов Иван ", "Практическое вождение 2", "01.01.23", "9:41"),

        )


    class ViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: Lesson) {
            binding.apply {
                tvTitle.text = lesson.title
                tvDescription.text = lesson.desc
                tvDate.text = lesson.date
                tvTime.text = lesson.time
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

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}