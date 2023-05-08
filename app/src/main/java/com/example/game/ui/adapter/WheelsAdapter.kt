package com.example.game.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.databinding.WheelItemBinding
import com.example.game.repository.WheelImages

class WheelsAdapter: ListAdapter<WheelImages, WheelsAdapter.ViewHolder>(COMPARATOR) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = WheelItemBinding.bind(itemView)
        fun bind(item: WheelImages) {
            binding.itemImage = item.image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wheel_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size
    override fun getItemId(position: Int): Long = currentList[position].id

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<WheelImages>() {
            override fun areItemsTheSame(oldItem: WheelImages, newItem: WheelImages): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: WheelImages, newItem: WheelImages): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
