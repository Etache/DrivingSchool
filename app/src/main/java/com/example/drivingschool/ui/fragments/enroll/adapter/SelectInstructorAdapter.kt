package com.example.drivingschool.ui.fragments.enroll.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.InstructorResponse
import com.example.drivingschool.databinding.InstructorInfoItemBinding
import com.squareup.picasso.Picasso

class SelectInstructorAdapter(private val data: List<InstructorResponse>) :
    RecyclerView.Adapter<SelectInstructorAdapter.SelectViewHolder>() {

    private val onItemClickListener: OnItemClickListener? = null

    class SelectViewHolder(private val binding: InstructorInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(instructor: InstructorResponse) {
            binding.tvName.text = instructor.name
            binding.tvSurname.text = instructor.surname
            binding.tvExpience.text = instructor.experience.toString()
            binding.rbRating.rating = instructor.rate!!.toFloat()

            val httpsImageUrl = instructor.profilePhoto?.replace("http://", "https://")
            Picasso.get()
                .load(httpsImageUrl)
                .placeholder(R.drawable.ic_default_photo)
                .into(binding.ivProfileImage)

//            Glide
//                .with(binding.ivProfileImage)
//                .load(instructor.profilePhoto)
//                .circleCrop()
//                .placeholder(R.drawable.default_pfp)
//                .into(binding.ivProfileImage)

            binding.ivInfo.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_selectInstructorFragment_to_instructorInfoFragment, bundleOf(ID_KEY to instructor.id))
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        return SelectViewHolder(
            InstructorInfoItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
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


    companion object {
        const val ID_KEY = "id"
    }
}
