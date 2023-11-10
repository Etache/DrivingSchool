package com.example.drivingschool.ui.fragments.enroll.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.databinding.InstructorProfileCommentItemBinding
import com.example.drivingschool.ui.fragments.enroll.MockCommentModel
import com.squareup.picasso.Picasso

class InstructorCommentAdapter(private val commentsList: List<MockCommentModel>): RecyclerView.Adapter<InstructorCommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(var binding: InstructorProfileCommentItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = InstructorProfileCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val comment = commentsList[position]

        holder.binding.tvName.text = comment.name
        holder.binding.tvSurname.text = comment.surname
        holder.binding.tvComment.text = comment.commentText

        val httpsImageUrl = comment.profilePhoto?.replace("http://", "https://")
        Picasso.get()
            .load(httpsImageUrl)
            .placeholder(R.drawable.ic_default_photo)
            .into(holder.binding.ivProfilePhoto)

//        Glide
//            .with(holder.binding.ivProfilePhoto)
//            .load(comment.profilePhoto)
//            .circleCrop()
//            .placeholder(R.drawable.default_pfp)
//            .into(holder.binding.ivProfilePhoto)

        holder.binding.tvComment.setLines(2)
        holder.binding.tvComment.setOnClickListener {
            if (holder.binding.tvComment.maxLines == 2) {
                holder.binding.tvComment.maxLines = Int.MAX_VALUE
            } else {
                holder.binding.tvComment.maxLines = 2
            }
        }
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }


}