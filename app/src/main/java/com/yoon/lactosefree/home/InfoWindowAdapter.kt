package com.yoon.lactosefree.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.yoon.lactosefree.R

class InfoWindowAdapter(context: Context, private val poiResolver: Map<Marker, POIItem>) : GoogleMap.InfoWindowAdapter {

    @SuppressLint("InflateParams")
    private var infoView: View = LayoutInflater.from(context).inflate(R.layout.window_map_info, null)
    private  var titleView: TextView

    init {
        with(infoView){
            titleView = findViewById(R.id.infoTitle)
        }
    }
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
    override fun getInfoContents(marker: Marker): View {
        val item = poiResolver[marker]
        titleView.text = item?.title
        return infoView
    }

}