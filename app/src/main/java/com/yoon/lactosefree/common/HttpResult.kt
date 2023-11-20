package com.yoon.lactosefree.common

import android.content.Context
import android.widget.Toast

sealed class HttpResult<T>(var vqlue: Any? = null, val message: String? = null) {
    data class Success<T> constructor(val resultValue: T, val msg: String? = null) : HttpResult<T>(resultValue)
    data class POIError<T>( //Blood 자체 rest 에러
        var httpCode: Int? = null,
        var msg: String? = null
    )  : HttpResult<T>(httpCode, msg)
    data class ExceptionError<T>( //네트워크 실행 중 예외
        var throwable:  Throwable? = null,
        var msg: String? = null
    ) : HttpResult<T>(throwable, msg)
    data class Loading<T>( //실행 중 여부
        var isLoading: Boolean = false,
        val msg: String? = null
    ) : HttpResult<T>(isLoading,msg )
}
fun toast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}