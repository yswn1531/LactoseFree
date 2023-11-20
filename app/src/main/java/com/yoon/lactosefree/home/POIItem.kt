package com.yoon.lactosefree.home

import com.google.gson.annotations.SerializedName

data class TMapPOISearchResult(var searchPoiInfo: TMapPOIInfo)

data class TMapPOIInfo(
    var totalCount: String = "",
    var count: String = "",
    var page: String = "",
    var pois: TMapPOIS
)
data class TMapPOIS(var poi: MutableList<POIItem> = mutableListOf())
data class POIItem(
    var id: String = "",

    @SerializedName("name")
    var title: String = "",
    var telNo: String = "",
    var frontLat: String = "",
    var frontLon: String = "",
    var noorLat: String = "",
    var noorLon: String = "",
    var upperAddrName: String = "",
    var middleAddrName: String = "",
    var lowerAddrName: String = "",
    var detailAddrName: String = "",
    var firstNo : String = "",
    var secondNo : String = "",

    var subtitle: String = "",

    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
) {
    override fun toString(): String {
        return title
    }
    fun updatePOIData() {
        subtitle = """$upperAddrName $middleAddrName  $lowerAddrName $detailAddrName $firstNo-$secondNo"""
        latitude = (frontLat.toDouble() + noorLat.toDouble()) / 2
        longitude = (frontLon.toDouble() + noorLon.toDouble()) / 2
    }
}

