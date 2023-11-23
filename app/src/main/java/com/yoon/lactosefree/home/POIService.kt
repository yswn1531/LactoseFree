package com.yoon.lactosefree.home

import com.yoon.lactosefree.common.ACCEPT
import com.yoon.lactosefree.common.APPKEY
import com.yoon.lactosefree.common.GET_FROM_TARGET_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface POIService {
    @GET(GET_FROM_TARGET_URL)
    suspend fun coroutineFlowPOIItemSelect(
        @Query("searchKeyword") searchKeyword: String,
        @Query("centerLat") centerLat: Double,
        @Query("centerLon") centerLon: Double
    ) : Response<TMapPOISearchResult>
}