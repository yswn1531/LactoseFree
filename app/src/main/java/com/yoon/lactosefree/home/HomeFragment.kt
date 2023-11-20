package com.yoon.lactosefree.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.yoon.lactosefree.R
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : ViewBindingBaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) ,
    OnMapReadyCallback {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("onCreateView", "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("onViewCreated", "onViewCreated")
        //퍼미션
        initMap()



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

        override fun onMapReady(naverMap: NaverMap) {
            Log.e("onMapReady", "onMapReady")
            this.naverMap = naverMap
            naverMap.locationSource = locationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow

            val mapx = 1268986587 / 1e7
            val mapy = 374715471 / 1e7
            val temp3 = LatLng(mapy,mapx)
            val temp2 = LatLng(37.47179, 126.8986)
            addMarker(naverMap)

        }

        companion object {
            private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        }

    private fun initMap(){
        //맵 옵션
        val options = NaverMapOptions()
        options.locationButtonEnabled(true)
        //여기에 위치받아서 설정
        //options.camera(CameraPosition(latLng, 15.0))
        val fragmentManager = childFragmentManager
        val mapFragment = fragmentManager.findFragmentById(R.id.mapFragment) as MapFragment?
            ?: MapFragment.newInstance(options).also { fragmentManager.beginTransaction().add(R.id.mapFragment, it).commit() }
        locationSource = FusedLocationSource(requireParentFragment(), LOCATION_PERMISSION_REQUEST_CODE)
        mapFragment.getMapAsync(this)

        //재사용하지 않고 새로 만듬
        /*val mapFragment = MapFragment.newInstance(options)
        fragmentManager.beginTransaction().replace(R.id.mapFragment, mapFragment).commit()
        mapFragment.getMapAsync(this)*/
        //locationSource = FusedLocationSource(requireParentFragment(), LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun addMarker(naverMap: NaverMap){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.poiFlow.collectLatest{ tMapPOISearchResult ->
                tMapPOISearchResult?.let { it
                    it.searchPoiInfo.pois.poi.also { poiItemList ->
                        for (item in poiItemList ){
                            item.updatePOIData()
                            with(Marker()){
                                position = LatLng(item.latitude, item.longitude)
                                map = naverMap
                                Log.e("PositionMarker",LatLng(item.latitude, item.longitude).toString())
                            }
                        }
                    }
                }
            }
        }
    }
}