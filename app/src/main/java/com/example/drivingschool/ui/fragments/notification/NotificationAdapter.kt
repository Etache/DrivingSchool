package com.example.drivingschool.ui.fragments.notification

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.data.models.notification.Notification
import com.example.drivingschool.data.models.notification.NotificationModel
import com.example.drivingschool.databinding.FragmentNotificationBinding
import com.example.drivingschool.databinding.ItemNotificationBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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
            binding.tvStatus.text = "Занятие ${notifications.status}"

            when (notifications.status) {
                "cancelled" -> {
                    //binding.tvStatus.setTextColor(R.color.red)
                    binding.tvStatus.setTextColor(Color.parseColor("#EB5757"))
                }

                "planned" -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#5883CB"))
                }

                else -> {
                    binding.tvStatus.setTextColor(Color.parseColor("#8E8E8E"))
                }
            }


            val dateString = notifications.created_at
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val date = format.parse(dateString)
            val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
            val formattedDate = outputFormat.format(date)
            binding.tvDate.text = formattedDate


            //           binding.tvDate.text=notifications.created_at

//            binding.tvName.text=notifications.name
//            binding.tvSurname.text=notifications.surname
//            val httpsImageUrl = notifications.profilePhoto.replace("http://", "https://")
//            Picasso.get()
//                .load(httpsImageUrl)
//                .placeholder(R.drawable.ic_default_photo)
//                .into(binding.ivProfile)

        }

    }

}