package com.n3t.mobile.ui.map.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.View

class SearchFineDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = object : RecyclerView.ViewHolder(View(parent.context)) {}
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun getItemCount(): Int = 0
}
