package com.n3t.mobile.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n3t.mobile.R
import com.n3t.mobile.core.widgets.GofaCell
import com.n3t.mobile.core.widgets.GofaCellCustom
import com.n3t.mobile.core.widgets.getBorderType
import com.n3t.mobile.data.model.search_map.ResultPlaceDetail
import com.n3t.mobile.utils.extensions.setSafeOnClickListener

class RecentPlaceAdapter(
    private val placeList: List<ResultPlaceDetail>,
    private val onSelectPlace: (ResultPlaceDetail) -> Unit
) : RecyclerView.Adapter<RecentPlaceAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_place_recent, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val place = placeList[position]
            holder.bind(place)
        }

        override fun getItemCount(): Int {
            return placeList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(place: ResultPlaceDetail) {
                val gofaCell = itemView as GofaCellCustom
                gofaCell.setTitle(place.name)
                gofaCell.setSubtitle(place.formattedAddress)

                val borderType = getBorderType(adapterPosition, placeList.size)
                gofaCell.setBorderType(borderType)

                gofaCell.setSafeOnClickListener {
                    onSelectPlace(place)
                }
            }
        }
}

