package com.example.drivingschool.ui.fragments.enroll.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.data.models.WorkWindows
import com.example.drivingschool.databinding.InstructorInfoItemBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class SelectInstructorAdapter(val onClick: (WorkWindows, String) -> Unit) :
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
            binding.tvExpience.text = instructor.experience.toString()
            binding.rbRating.rating = instructor.rate!!.toFloat()

            val httpsImageUrl = instructor.profilePhoto //replace("http://", "https://")
            Picasso.get()
                .load(httpsImageUrl)
                .placeholder(R.drawable.ic_default_photo)
                .into(binding.ivProfileImage)

            binding.ivInfo.setOnClickListener {
                it.findNavController()
                    .navigate(
                        R.id.action_selectInstructorFragment_to_instructorInfoFragment,
                        bundleOf(ID_KEY to instructor.id)
                    )
            }
            itemView.setOnClickListener {
                onClick(instructor.workwindows!!, "${instructor.name} ${instructor.surname} ${instructor.lastname}")
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

    companion object {
        const val ID_KEY = "id"
    }
}
