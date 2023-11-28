package com.yoon.lactosefree.home

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.yoon.lactosefree.common.DefaultApplication
import com.yoon.lactosefree.common.RetrofitOkHttpManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 해야 할 일
 * 1. 현재 나의 좌표 데이터
 *  1.1 현재 ViewModel 에서 받음
 * 2. 현재 나의 좌표 데이터를 넣은 POI 데이터
 *  2.1 구현 된 부분 : 좌표 데이터를 30초 마다 받아서 POI 데이터 요청
 *  2.2 미구현 부분 : 브랜드 목록에 있는 모든 POI 요청 해서 받아옴
 * 3. 마커에 추가할 브랜드 이미지
 *  3.1 InfoWindow 에 추가할 이미지
 *  3.2 Marker 를 대신할 이미지
 */


class HomeViewModel : ViewModel() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var _currentLocationResult = MutableStateFlow<Location?>(null)
    val currentLocationResult : StateFlow<Location?>
        get() = _currentLocationResult

    //내부에서 사용할 poiFlow
    private var _poiFlow = MutableStateFlow<TMapPOISearchResult?>(null)
    //외부에서 접근할 Flow
    val poiFlow: StateFlow<TMapPOISearchResult?>
        get() = _poiFlow

    val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            CoroutineScope(Dispatchers.IO).launch {
                flow<Location> {
                    _currentLocationResult.emit(locationResult.locations[0])
                    coroutineFlowSelectPOI(LatLng(locationResult.locations[0].latitude, locationResult.locations[0].longitude))
                }.stateIn(CoroutineScope(Dispatchers.IO))
            }
        }
    }

    private fun coroutineFlowSelectPOI(latLng: LatLng) {
        RetrofitOkHttpManager.poiCoroutineScope.launch {
            flow<TMapPOISearchResult> {
                val response = RetrofitOkHttpManager.poiService.coroutineFlowPOIItemSelect(
                    "스타벅스",
                    latLng.latitude,
                    latLng.longitude
                )
                if (response.isSuccessful){
                    _poiFlow.emit(response.body())
                }else{
                    Log.e("---------","Response Error")
                }
            }.stateIn(RetrofitOkHttpManager.poiCoroutineScope)
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(){
        val locationIntervalTime = 30000L
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, locationIntervalTime)
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(locationIntervalTime) //위치 획득 후 update 되는 최소 주기
            .setMaxUpdateDelayMillis(locationIntervalTime).build() //위치 획득 후 update delay 최대 주기
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(DefaultApplication.applicationContext())
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun destroyReCallback(){
        if(::fusedLocationClient.isInitialized){
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}



