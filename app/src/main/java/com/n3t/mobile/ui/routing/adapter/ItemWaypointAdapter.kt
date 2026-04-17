package com.n3t.mobile.ui.routing.adapter
import androidx.recyclerview.widget.RecyclerView
class ItemWaypointAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RecyclerView.ViewHolder = object : RecyclerView.ViewHolder(android.view.View(parent.context)) {}
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun getItemCount(): Int = 0
}
