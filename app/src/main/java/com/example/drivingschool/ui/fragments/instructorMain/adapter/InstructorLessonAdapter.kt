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

    private var lessons = arrayListOf<LessonsItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(it: List<LessonsItem>) {
        lessons = it as ArrayList<LessonsItem>
        notifyDataSetChanged()
        Log.d("ahahaha", "InstructorLessonAdapter updateList: ${it}")
    }

    inner class LessonViewHolder(private val binding: InstructorMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(lesson: LessonsItem) {
            binding.apply {
                tvTitle.text = "${lesson.student?.surname} ${lesson.student?.name} ${lesson.student?.lastname}"
                tvDate.text = lesson.date
                tvTime.text = lesson.time
            }

            itemView.setOnClickListener {
                onClick(lesson.id.toString())
            }
            Log.d("ahahaha", "установлены данные на странице инструктора : ${lesson.student?.name} ${lesson.student?.surname}")
            Log.d("ahahaha", "InstructorLessonAdapter: ${lesson}")
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

    override fun getItemCount() = lessons.size

    companion object {
        const val ID_KEY = "id"
    }
}