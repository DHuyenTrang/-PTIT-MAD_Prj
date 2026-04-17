package com.n3t.mobile.ui.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.n3t.mobile.R

class LicensePlateAdapter(private val listLicensePlate: List<String>, private val callback : CallBackDeleteLicensePlate): Adapter<LicensePlateAdapter.LicensePlateViewHolder>() {
    class LicensePlateViewHolder(itemView: View) : ViewHolder(itemView) {
        val tvEditLicensePlate: TextView = itemView.findViewById(R.id.tvDeleteLicensePlate)
        val btnDeleteLicensePlate: Button = itemView.findViewById(R.id.btnDeleteLicensePlate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicensePlateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_license_plate, parent, false)
        return LicensePlateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLicensePlate.size
    }

    override fun onBindViewHolder(holder: LicensePlateViewHolder, position: Int) {
        val data : String = listLicensePlate[position]
        holder.tvEditLicensePlate.text = data
        holder.btnDeleteLicensePlate.setOnClickListener {
            callback.clickDelete(position)
        }
    }
}
interface CallBackDeleteLicensePlate{
    fun clickDelete(position : Int)
}
