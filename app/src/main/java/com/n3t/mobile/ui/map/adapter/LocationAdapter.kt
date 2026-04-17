package com.n3t.mobile.ui.map.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.n3t.mobile.R
import com.n3t.mobile.data.model.license_plate.Location
import com.n3t.mobile.data.model.search_map.ResultPlaceDetail

class LocationAdapter(private val listLocation: List<ResultPlaceDetail>,
                      private val isEdit: MutableLiveData<Boolean>,
                      val context: Context,
                      private val callback: OnCallBackDeletePlace
): Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(itemView: View) : ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvNamePlace: TextView = itemView.findViewById(R.id.tvNamePlace)
        val tvNameLocation: TextView = itemView.findViewById(R.id.tvNameLocation)
        val btnSeeAll: Button = itemView.findViewById(R.id.btnSeeAll)
        val tbSeeAll: TableRow = itemView.findViewById(R.id.tbSeeAll)
        val btnDeletePlace: Button = itemView.findViewById(R.id.btnDeletePlace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLocation.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {

        if (isEdit.value == true){
            holder.btnSeeAll.visibility = View.GONE
            holder.btnDeletePlace.visibility = View.VISIBLE
        }else{
            holder.btnSeeAll.visibility = View.VISIBLE
            holder.btnDeletePlace.visibility = View.GONE
        }

        val data = listLocation[position]
        holder.ivIcon.setImageResource(R.drawable.ic_place)
        holder.tvNamePlace.text = data.customName
        holder.tvNameLocation.text = data.formattedAddress
        if (holder.tvNameLocation.text == "") {
            holder.tvNameLocation.visibility = View.GONE
        }
        holder.btnSeeAll.setOnClickListener {
            callback.goToRoutingActivity(position)
        }
        holder.tbSeeAll.setOnClickListener {
            callback.goToRoutingActivity(position)
        }

        holder.btnDeletePlace.setOnClickListener{
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_delete_saved_location)
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog_delete_saved_location)
            val btnClose = dialog.findViewById<TextView>(R.id.tvClose)
            val btnDelete = dialog.findViewById<Button>(R.id.btnDelete)
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            btnDelete.setOnClickListener {
                callback.onDeletePlace(data)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

}
interface OnCallBackDeletePlace{

    fun goToRoutingActivity(position: Int)
    fun onDeletePlace(data: ResultPlaceDetail)
}
