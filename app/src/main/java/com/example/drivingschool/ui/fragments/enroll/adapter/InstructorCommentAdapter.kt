package com.example.drivingschool.ui.fragments.enroll.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.FeedbackInstructor
import com.example.drivingschool.databinding.InstructorProfileCommentItemBinding
import com.squareup.picasso.Picasso
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class InstructorCommentAdapter(private var commentsList: List<FeedbackInstructor>?) :
    RecyclerView.Adapter<InstructorCommentAdapter.CommentViewHolder>() {
    class CommentViewHolder(var binding: InstructorProfileCommentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = InstructorProfileCommentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        if (commentsList != null){
            val comment = commentsList!![position]

            holder.binding.tvName.text = comment.student?.name
            holder.binding.tvSurname.text = comment.student?.surname
            holder.binding.tvComment.text = comment.text

            val dateString = comment.createdAt
            val dateTime = OffsetDateTime.parse(dateString)
            val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru", "RU"))
            val formattedDate = dateTime.format(formatter)

            holder.binding.tvCommentDate.text = formattedDate
            holder.binding.rbRating.rating = comment.mark?.toFloat()!!
            val httpsImageUrl = comment.student?.profilePhoto?.big
            Picasso.get()
                .load(httpsImageUrl)
                .placeholder(R.drawable.ic_default_photo)
                .into(holder.binding.ivProfilePhoto)

            holder.binding.tvComment.setLines(2)
            holder.binding.tvComment.setOnClickListener {
                if (holder.binding.tvComment.maxLines == 2) {
                    holder.binding.tvComment.maxLines = Int.MAX_VALUE
                } else {
                    holder.binding.tvComment.maxLines = 2
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(commentsList != null) {
            commentsList!!.size
        } else {
            0
        }
    }

}