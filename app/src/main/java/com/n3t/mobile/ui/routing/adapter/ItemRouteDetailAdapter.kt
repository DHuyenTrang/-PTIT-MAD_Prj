package com.n3t.mobile.ui.routing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.n3t.mobile.R
import com.n3t.mobile.data.model.place_flow.RouteOptionUiModel
import com.n3t.mobile.databinding.ItemRoutingDetailBinding
import com.n3t.mobile.utils.extensions.setSafeOnClickListener

class ItemRouteDetailAdapter(
    private val onRouteSelected: (RouteOptionUiModel) -> Unit,
) : RecyclerView.Adapter<ItemRouteDetailAdapter.ViewHolder>() {

    private val items = mutableListOf<RouteOptionUiModel>()

    fun submitList(routes: List<RouteOptionUiModel>) {
        items.clear()
        items.addAll(routes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRoutingDetailBinding.inflate(
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
        private val binding: ItemRoutingDetailBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RouteOptionUiModel) {
            binding.txtDuration.text = "${item.durationText} (${item.distanceText})"
            binding.txtDistance.text = item.label
            val backgroundRes = if (item.isSelected) {
                R.drawable.bg_route_card_selected
            } else {
                R.drawable.bg_route_card_unselected
            }
            binding.layoutRootView.setBackgroundResource(backgroundRes)
            val titleColor = if (item.isSelected) R.color.onPrimaryContainer else R.color.primary
            val bodyColor = if (item.isSelected) R.color.onPrimaryContainer else R.color.onSurfaceVariant
            binding.txtDuration.setTextColor(ContextCompat.getColor(binding.root.context, titleColor))
            binding.txtDistance.setTextColor(ContextCompat.getColor(binding.root.context, bodyColor))
            binding.layoutRootView.setSafeOnClickListener {
                onRouteSelected(item)
            }
        }
    }
}
