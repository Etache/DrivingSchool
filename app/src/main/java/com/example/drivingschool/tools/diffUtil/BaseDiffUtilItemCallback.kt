package com.example.drivingschool.tools.diffUtil

import androidx.recyclerview.widget.DiffUtil

class BaseDiffUtilItemCallback<T : BaseDiffModel<S>, S> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}
