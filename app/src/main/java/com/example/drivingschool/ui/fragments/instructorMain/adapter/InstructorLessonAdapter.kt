package com.example.drivingschool.ui.fragments.instructorMain.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.databinding.InstructorMainItemBinding


class InstructorLessonAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<InstructorLessonAdapter.LessonViewHolder>() {

    private var lessons = arrayListOf<LessonsItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(it: List<LessonsItem>) {
        lessons = it as ArrayList<LessonsItem>
        notifyDataSetChanged()
    }

    inner class LessonViewHolder(private val binding: InstructorMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(lesson: LessonsItem) {
            Log.e("ahahaha", "InstructorLessonAdapter: ${lesson}", )
            binding.apply {
                tvTitle.text = "${lesson.student?.surname} ${lesson.student?.name} ${lesson.student?.lastname}"
                tvDate.text = lesson.date
                tvTime.text = lesson.time

                val status = lesson.status
                val statusString = tvLessonStatus
                when(status) {
                    "finished" -> {
                        statusString.text = "Завершен"
                        statusString.setTextColor(R.color.blue)
                    }
                    "active" -> {
                        statusString.text = "Активен"
                        statusString.setTextColor(R.color.green)
                    }
                    "planned" -> {
                        statusString.text = "Запланирован"
                        statusString.setTextColor(R.color.blue)
                    }
                    "canceled" -> {
                        statusString.text = "Отменен"
                        statusString.setTextColor(R.color.red)
                    }
                }
            }

            itemView.setOnClickListener {
                it.findNavController().navigate(R.id.instructorCurrentLessonFragment,
                    bundleOf(ID_KEY to lesson.id)
                )

                onClick(lesson.id.toString())
            }

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