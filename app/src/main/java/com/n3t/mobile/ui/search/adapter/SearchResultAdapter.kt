package com.n3t.mobile.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.n3t.mobile.data.model.place_flow.PlaceSuggestionUiModel
import com.n3t.mobile.databinding.ItemSearchPlaceResultBinding
import com.n3t.mobile.utils.RouteFormatUtils
import com.n3t.mobile.utils.extensions.setSafeOnClickListener

class SearchResultAdapter(
    private val onSelectPlace: (PlaceSuggestionUiModel) -> Unit,
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val items = mutableListOf<PlaceSuggestionUiModel>()

    fun submitList(newItems: List<PlaceSuggestionUiModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchPlaceResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ItemSearchPlaceResultBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlaceSuggestionUiModel) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.subtitle
            binding.tvDistance.isVisible = item.distanceMeters != null
            binding.tvDistance.text = item.distanceMeters?.let(RouteFormatUtils::formatDistance).orEmpty()
            binding.viewItemSearch.setSafeOnClickListener {
                onSelectPlace(item)
            }
        }
    }
}
