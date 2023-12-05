package com.example.drivingschool.ui.fragments.notification

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.data.models.notification.Notification
import com.example.drivingschool.databinding.ItemNotificationBinding
import com.example.drivingschool.ui.fragments.studentMain.lesson.LessonStatus
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(private var notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate
                (LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        return holder.onBind(notificationList[position])
    }

    class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(notifications: Notification) {
            val context = binding.root.context
            when (notifications.status) {
                LessonStatus.CANCELED.status -> {
                    binding.tvStatus.text = context.getString(R.string.lesson_cancelled)
                    binding.tvStatus.setTextColor(Color.parseColor("#EB5757"))
                }

                LessonStatus.PLANNED.status -> {
                    binding.tvStatus.text = context.getString(R.string.lesson_planed)
                    binding.tvStatus.setTextColor(Color.parseColor("#5883CB"))
                }

                else -> {
                    binding.tvStatus.text = notifications.status
                    binding.tvStatus.setTextColor(Color.parseColor("#8E8E8E"))
                }
            }

            if (notifications.isRead == false) {
                binding.tvNewNotification.visibility = View.VISIBLE
            } else {
                binding.tvNewNotification.visibility = View.GONE
            }

            binding.tvName.text = notifications.lesson?.student?.name
            binding.tvSurname.text = notifications.lesson?.student?.surname
            binding.tvNumber.text = notifications.lesson?.student?.phoneNumber
            binding.tvDate.text = formatDate(notifications.lesson?.date)


            val httpsImageUrl =
                notifications.lesson?.student?.profilePhoto?.big
            Picasso.get()
                .load(httpsImageUrl)
                .placeholder(R.drawable.ic_default_photo)
                .into(binding.ivProfile)

        }

        private fun formatDate(inputDate: String?): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputDate?.let { inputFormat.parse(it) } ?: return ""

            val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
            return outputFormat.format(date).replaceFirstChar { it.uppercase() }
        }
    }


}