package com.n3t.mobile.ui.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.n3t.mobile.R
import com.n3t.mobile.data.model.license_plate.TrafficNews

class TrafficNewsAdapter(private val listTrafficNews: ArrayList<TrafficNews>) :
    Adapter<TrafficNewsAdapter.TrafficNewsViewHolder>() {
    class TrafficNewsViewHolder(itemView: View) : ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvLink: TextView = itemView.findViewById(R.id.tvLink)
        val ivImage1: ImageView = itemView.findViewById(R.id.ivImage1)
        val ivImage2: ImageView = itemView.findViewById(R.id.ivImage2)
        val ivImage3: ImageView = itemView.findViewById(R.id.ivImage3)
        val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
        val tvChatCount: TextView = itemView.findViewById(R.id.tvChatCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrafficNewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_traffic_news, parent, false)
        return TrafficNewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTrafficNews.size
    }

    override fun onBindViewHolder(holder: TrafficNewsViewHolder, position: Int) {
        val data = listTrafficNews[position]
        holder.ivAvatar.setImageResource(data.avatar)
        holder.tvName.text = data.name
        holder.tvTime.text = data.time
        holder.tvTitle.text = data.title
        holder.tvLink.text = data.link
        holder.ivImage1.setImageResource(data.img1)
        holder.ivImage2.setImageResource(data.img2)
        holder.ivImage3.setImageResource(data.img3)
        holder.tvLikeCount.text = data.likeCount.toString()
        holder.tvChatCount.text = data.chatCount.toString()
    }
}

