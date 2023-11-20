package com.yoon.lactosefree.home

import androidx.lifecycle.ViewModel
import com.yoon.lactosefree.common.RetrofitOkHttpManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 필요한 데이터
 * 1. 현재 나의 좌표 데이터
 * 2. 현재 나의 좌표 데이터를 넣은 POI 데이터
 *
 * 해야 할 일
 * 1. 현재 나의 좌표 데이터를 얻는 LocationProvider에서 좌표를 받아옴
 * 2. RetrofitOkHttpManager를 통해 flow로 값을 받음
 */
class HomeViewModel : ViewModel() {


    //내부에서 사용할 poiFlow
    private var _poiFlow = MutableStateFlow<TMapPOISearchResult?>(null)
    //외부에서 접근할 Flow
    val poiFlow: StateFlow<TMapPOISearchResult?>
        get() = _poiFlow

    init {
        coroutineFlowSelectPOI()
    }

    private fun coroutineFlowSelectPOI() {
        RetrofitOkHttpManager.poiCoroutineScope.launch {
            flow<TMapPOISearchResult> {
                val response =
                RetrofitOkHttpManager.poiService.coroutineFlowPOIItemSelect(
                    "스타벅스",
                    37.4724,
                    126.8961
                )
                if (response.isSuccessful){
                    _poiFlow.emit(response.body())
                }else{

                }
            }.stateIn(RetrofitOkHttpManager.poiCoroutineScope)
        }
    }
}



