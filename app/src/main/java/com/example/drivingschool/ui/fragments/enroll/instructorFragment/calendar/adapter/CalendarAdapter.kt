package com.example.drivingschool.ui.fragments.enroll.instructorFragment.calendar.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.drivingschool.R
import com.example.drivingschool.databinding.ItemCalendarBinding

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private val itemColors = IntArray(daysOfMonth.size)
    private var dates = arrayListOf<String>()

    fun setItemColor(position: Int, colorResId: Int) {
        itemColors[position] = colorResId
    }

    fun setDates(list : ArrayList<String>){
        dates.addAll(list)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_calendar, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666).toInt()
        val binding =
            ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding, onItemListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonth[position]
        holder.dayOfMonth.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
    }

    override fun getItemCount(): Int = daysOfMonth.size

    interface OnItemListener {
        fun onItemClick(position: Int, itemText: String)
    }

    inner class CalendarViewHolder(
        private val binding: ItemCalendarBinding,
        private val onItemListener: CalendarAdapter.OnItemListener
    ) : ViewHolder(binding.root), View.OnClickListener {

        val dayOfMonth: TextView = binding.itemCalendarDay
        private val onItemClickListener = onItemListener

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClickListener.onItemClick(bindingAdapterPosition, dayOfMonth.text.toString())
        }

        fun bind(itemText: String) {
            binding.itemCalendarDay.text = itemText
            //change text color for active dates
            dayOfMonth.setTextColor(Color.parseColor(R.color.blue.toString()))

        }
    }
}