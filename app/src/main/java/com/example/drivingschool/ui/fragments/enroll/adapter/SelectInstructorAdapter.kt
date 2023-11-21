package com.example.drivingschool.ui.fragments.enroll.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.Date
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.databinding.InstructorInfoItemBinding
import com.example.drivingschool.ui.fragments.BundleKeys
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
            binding.tvName.text = instructor.name
            binding.tvSurname.text = instructor.surname

            when (val experience = instructor.experience) {
                in 1..4 -> {
                    binding.tvExpience.text = "$experience года"
                }
                in 5..9 -> {
                    binding.tvExpience.text = "$experience лет"
                }
                else -> {
                    binding.tvExpience.text = "$experience лет"
                }
            }
            binding.rbRating.rating = instructor.rate!!.toFloat()

            val httpsImageUrl = instructor.profilePhoto?.small //replace("http://", "https://")
            Picasso.get()
                .load(httpsImageUrl)
                .placeholder(R.drawable.ic_default_photo)
                .into(binding.ivProfileImage)

            binding.ivInfo.setOnClickListener {
                it.findNavController()
                    .navigate(
                        R.id.action_selectInstructorFragment_to_instructorInfoFragment,
                        bundleOf(BundleKeys.ID_KEY to instructor.id)
                    )
            }
            itemView.setOnClickListener {
                onClick(instructor.workwindows, "${instructor.name} ${instructor.surname} ${instructor.lastname}", instructor.id.toString())
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
