package com.example.drivingschool.ui.fragments.enroll.student.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.Date
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.databinding.InstructorInfoItemBinding
import com.example.drivingschool.ui.fragments.Constants
import com.squareup.picasso.Picasso

class SelectInstructorAdapter(val onClick : (workWindows: ArrayList<Date>, name : String, id : String) -> Unit) :
    RecyclerView.Adapter<SelectInstructorAdapter.SelectViewHolder>() {

    private var instructors = arrayListOf<InstructorResponse>()

    fun updateList(it: List<InstructorResponse>) {
        instructors = it as ArrayList<InstructorResponse>
        notifyDataSetChanged()
    }

    inner class SelectViewHolder(private val binding: InstructorInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(instructor: InstructorResponse) {
            val context = binding.root.context
            binding.tvName.text = instructor.name
            binding.tvSurname.text = instructor.surname

            when (val experience = instructor.experience) {
                1 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_year, experience)
                }
                in 1..4 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 22..24 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 32..34 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                } in 42..44 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 52..54 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 62..64 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 72..74 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 82..84 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 92..94 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_2__4, experience)
                }
                in 5..9 -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_5__9, experience)
                }
                else -> {
                    binding.tvExpience.text = context.getString(R.string.experience_years_5__9, experience)
                }
            }
            binding.rbRating.rating = instructor.rate!!.toFloat()

            val httpsImageUrl = instructor.profilePhoto?.medium
            Picasso.get()
                .load(httpsImageUrl)
                .placeholder(R.drawable.ic_default_photo)
                .into(binding.ivProfileImage)

            binding.ivInfo.setOnClickListener {
                it.findNavController()
                    .navigate(
                        R.id.action_selectInstructorFragment_to_instructorInfoFragment,
                        bundleOf(Constants.ID_KEY to instructor.id)
                    )
            }
            itemView.setOnClickListener {
                if(instructor.workwindows != null){
                    onClick(instructor.workwindows!!, "${instructor.name} ${instructor.surname} ${instructor.lastname}", instructor.id.toString())
                } else {
                    Toast.makeText(context, "Инструктор еще не создал расписание", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        return SelectViewHolder(
            InstructorInfoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return instructors.size
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val instructor = instructors[position]
        holder.bind(instructor)
    }

}
