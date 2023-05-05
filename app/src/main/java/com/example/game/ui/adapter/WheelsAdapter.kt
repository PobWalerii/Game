package com.example.game.ui.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.R
import com.example.game.databinding.WheelItemBinding

@SuppressLint("NotifyDataSetChanged")
class WheelsAdapter : RecyclerView.Adapter<WheelsAdapter.ViewHolder>() {

    private var listImages: List<Drawable> = emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = WheelItemBinding.bind(itemView)
        fun bind(item: Drawable) {
            binding.itemImage = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wheel_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listImages[position])
    }

    override fun getItemCount() = listImages.size

    fun setImagesList(list: List<Drawable>) {
        listImages = list
        notifyDataSetChanged()
    }
}