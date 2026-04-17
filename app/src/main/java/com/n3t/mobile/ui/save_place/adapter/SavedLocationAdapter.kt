package com.n3t.mobile.ui.save_place.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.n3t.mobile.R
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel
import com.n3t.mobile.databinding.ItemSavedLocationBinding

class SavedLocationAdapter(
    private val onItemClick: (PlaceDetailUiModel) -> Unit
) : ListAdapter<PlaceDetailUiModel, SavedLocationAdapter.ViewHolder>(DiffCallback()) {

    var isEditMode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position == itemCount - 1)
    }

    inner class ViewHolder(private val binding: ItemSavedLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlaceDetailUiModel, isLast: Boolean) {
            binding.ivIcon.setImageResource(R.drawable.ic_bookmark)
            binding.tvNamePlace.text = item.name
            binding.tvNameLocation.text = item.formattedAddress

            // Show divider for non-last items (within the RV card)
            binding.divider.visibility = if (isLast) View.GONE else View.VISIBLE

            binding.root.setOnClickListener { onItemClick(item) }
            
            // In edit mode, we might want to change the chevron or appearance, 
            // but per screenshot it stays mostly the same, just the click action changes in fragment.
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlaceDetailUiModel>() {
        override fun areItemsTheSame(oldItem: PlaceDetailUiModel, newItem: PlaceDetailUiModel): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(oldItem: PlaceDetailUiModel, newItem: PlaceDetailUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
