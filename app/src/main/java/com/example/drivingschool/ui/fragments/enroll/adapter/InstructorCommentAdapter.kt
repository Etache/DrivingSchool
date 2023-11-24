package com.example.drivingschool.ui.fragments.enroll.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.FeedbackInstructor
import com.example.drivingschool.databinding.InstructorProfileCommentItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class InstructorCommentAdapter(private var commentsList: List<FeedbackInstructor>) :
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


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val comment = commentsList[position]

        holder.binding.tvName.text = comment.student.name
        holder.binding.tvSurname.text = comment.student.surname
        holder.binding.tvComment.text = comment.text

        val dateString = comment.createdAt
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(dateString)
        val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val formattedDate = outputFormat.format(date)

        holder.binding.tvCommentDate.text = formattedDate
        holder.binding.rbRating.rating = comment.mark.toFloat()
        val httpsImageUrl = comment.student.profilePhoto?.small?.replace("http://", "https://")
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

    override fun getItemCount(): Int {
        return commentsList.size
    }


}