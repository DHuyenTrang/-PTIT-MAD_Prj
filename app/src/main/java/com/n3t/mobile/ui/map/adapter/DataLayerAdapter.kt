package com.n3t.mobile.ui.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.n3t.mobile.databinding.ItemMapTypeBinding

class DataLayerAdapter(
    val onClick: (MapOption) -> Unit,
): ListAdapter<MapOption, DataLayerAdapter.DataLayerViewHolder>(MapOptionDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataLayerViewHolder {
        val binding = ItemMapTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataLayerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DataLayerViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class DataLayerViewHolder(private val binding: ItemMapTypeBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onClick(getItem(adapterPosition))
                }
            }
        }

        fun bind(item: MapOption) {
            if (item.isSelected) binding.viewSelected.visibility = View.VISIBLE
            else binding.viewSelected.visibility = View.GONE

            binding.imgType.setImageResource(item.icon)
            binding.tvType.text = item.title
        }
    }
}
