package com.n3t.mobile.ui.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.n3t.mobile.R
import com.n3t.mobile.databinding.ItemMapTypeBinding
import com.n3t.mobile.utils.extensions.updateBackground
import com.n3t.mobile.utils.extensions.updateTextColorRes

data class MapOption(
    val icon: Int,
    val title: String,
    val isSelected: Boolean = false
)

class MapTypeAdapter(
    val onClick: (MapOption) -> Unit
): ListAdapter<MapOption, MapTypeAdapter.MapOptionViewHolder>(MapOptionDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MapOptionViewHolder {
        val binding = ItemMapTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MapOptionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MapOptionViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class MapOptionViewHolder(private val binding: ItemMapTypeBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(adapterPosition)
                    if (!item.isSelected) {
                        onClick(item)
                    }
                }
            }
        }

        fun bind(item: MapOption) {
            if (item.isSelected){
                binding.viewSelected.visibility = View.VISIBLE
                binding.viewSelected.setBackgroundResource(0)
                binding.viewSelected.setBackgroundResource(R.drawable.bg_map_type_selected)
            }
            else binding.viewSelected.visibility = View.GONE

            binding.imgType.setImageResource(item.icon)
            binding.tvType.text = item.title
            binding.tvType.updateTextColorRes(R.color.onBackground)
        }
    }
}

class MapOptionDiffCallback : DiffUtil.ItemCallback<MapOption>() {
    override fun areItemsTheSame(oldItem: MapOption, newItem: MapOption): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: MapOption, newItem: MapOption): Boolean {
        return oldItem == newItem
    }
}
