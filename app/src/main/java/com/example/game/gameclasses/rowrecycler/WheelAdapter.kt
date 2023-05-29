package com.example.game.gameclasses.rowrecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.databinding.WheelItemBinding
import com.example.game.data.ItemImages

class WheelAdapter: ListAdapter<ItemImages, WheelAdapter.ViewHolder>(COMPARATOR) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = WheelItemBinding.bind(itemView)
        fun bind(item: ItemImages) {
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
    override fun getItemId(position: Int): Long = currentList[position].idPosition

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ItemImages>() {
            override fun areItemsTheSame(oldItem: ItemImages, newItem: ItemImages): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemImages, newItem: ItemImages): Boolean {
                return oldItem.idPosition == newItem.idPosition
            }
        }
    }


}

