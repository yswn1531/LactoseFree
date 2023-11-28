package com.yoon.lactosefree.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yoon.lactosefree.R
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * 부족한 부분
 *  1. 앱을 켰을 때 처음 카메라 위치가 null 값에서 현재 위치로 이동함
 */

class HomeFragment : ViewBindingBaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    OnMapReadyCallback, OnInfoWindowClickListener, OnMarkerClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    var poiResolver: MutableMap<Marker, POIItem> = HashMap()
    private var markerList = mutableListOf<Marker>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("onCreateView", "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("onViewCreated", "onViewCreated")
        //퍼미션
        viewModel.getCurrentLocation()
        initMap()

    }

    private fun initMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        mapUi(googleMap)
        moveCameraOnCurrentLocation()
        initMarker()
    }

    @SuppressLint("MissingPermission")
    private fun mapUi(googleMap: GoogleMap) {
        with(googleMap) {
            isMyLocationEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            setOnInfoWindowClickListener(this@HomeFragment)
            setInfoWindowAdapter(InfoWindowAdapter(requireContext(), poiResolver))
        }
    }

    private fun initMarker() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.poiFlow.collectLatest { tMapPOISearchResult ->
                tMapPOISearchResult?.let {
                    it.searchPoiInfo.pois.poi.also { poiItemList ->
                        removePoiMarker()
                        for (item in poiItemList) {
                            item.updatePOIData()
                            val position = LatLng(item.latitude, item.longitude)
                            addMapPoiMarker(position, item)
                        }
                    }
                }
            }
        }
    }

    private fun addMapPoiMarker(position: LatLng, data: POIItem) {
        val options = MarkerOptions()
        with(options) {
            position(position)
            icon(BitmapDescriptorFactory.fromResource(R.drawable.im_marker_startbucks_48))
        }
        val marker = googleMap.addMarker(options)                 //마커를 더함
        poiResolver[marker as Marker] = data
        markerList.add(marker)
    }

    private fun removePoiMarker(){
        for (currentIndex in 0 until markerList.size) {
           val marker = markerList[currentIndex]
            marker.remove()
        }
        markerList.clear()
    }

    override fun onInfoWindowClick(marker: Marker) {
        //클릭한 마커 객체의 poiItem의 title의 첫번째
        val action = HomeFragmentDirections.actionHomeFragmentToBrandMenuFragment("스타벅스")
        findNavController().navigate(action)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.showInfoWindow()
        return true
    }

    fun moveCameraOnCurrentLocation(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.currentLocationResult.collectLatest{
                    it?.let {
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f)
                        )
                    }
                }
            }
        }
    }

    /* private val permissionListener = object : PermissionListener {
      override fun onPermissionGranted() {

      }

      override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
          Toast.makeText(context, "위치제공 허락을 해야 앱이 정상적으로 작동합니다", Toast.LENGTH_SHORT)
              .show()
          requireActivity().finish()
      }
  }
  private fun checkMyPermissionLocation() {
      TedPermission.create()
          .setPermissionListener(permissionListener)
          .setRationaleMessage("지도를 사용하기 위해서는 위치제공 허락이 필요합니다")
          .setPermissions(
              Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION
          )
          .check()
  }*/

    override fun onStop() {
        super.onStop()
        viewModel.destroyReCallback()
    }

}