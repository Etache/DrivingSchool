package com.example.drivingschool.ui.fragments.enroll.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drivingschool.R
import com.example.drivingschool.data.models.InstructorRequest
import com.example.drivingschool.databinding.InstructorInfoItemBinding

class SelectInstructorAdapter(private val data: List<InstructorRequest>) :
    RecyclerView.Adapter<SelectInstructorAdapter.SelectViewHolder>() {

    private val onItemClickListener: OnItemClickListener? = null

    class SelectViewHolder(private val binding: InstructorInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(instructor: InstructorRequest) {
            binding.tvName.text = instructor.name
            binding.tvSurname.text = instructor.surname
            binding.tvExpience.text = instructor.experience.toString()
            binding.rbRating.rating = instructor.rate!!.toFloat()

            Glide
                .with(binding.ivProfileImage)
                .load(instructor.profilePhoto)
                .circleCrop()
                .placeholder(R.drawable.default_pfp)
                .into(binding.ivProfileImage)

            binding.ivInfo.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_selectInstructorFragment_to_instructorInfoFragment)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        return SelectViewHolder(
            InstructorInfoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val instructor = data[position]
        holder.bind(instructor)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
