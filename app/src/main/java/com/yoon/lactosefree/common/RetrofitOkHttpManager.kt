package com.yoon.lactosefree.common

import android.util.Log
import com.yoon.lactosefree.home.POIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

const val THREAD_VALUE = 5
object RetrofitOkHttpManager {
    private var okHttpClient: OkHttpClient

    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(TARGET_URL)

    val poiService: POIService
        get() = retrofitBuilder.build().create(POIService::class.java)

    /**
     * 현 프로젝트에서 사용할 코루틴 스코프 및 디스페처 정의
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private val poiRESTDispatchers: CoroutineDispatcher
        get() = Dispatchers.IO.limitedParallelism(THREAD_VALUE)
    val poiCoroutineScope: CoroutineScope
        get() = CoroutineScope(poiRESTDispatchers)

    init {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request = chain.request()
                val newRequest: Request = request.newBuilder()
                    .addHeader("Accept", ACCEPT)        //Header 설정
                    .addHeader("appKey", APPKEY)
                    .build()
                chain.proceed(newRequest)
            }).addInterceptor(RetryInterceptor())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS).build()
        retrofitBuilder.client(okHttpClient) //OkHttp 와 연동
    }
    private class RetryInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            var response: Response = chain.proceed(request)
            var tryCount = 0
            val maxLimit = 2
            while (!response.isSuccessful && tryCount < maxLimit) {
                Log.d("TAG", "요청 실패 - $tryCount")
                tryCount++
                response = chain.proceed(request)
            }
            return response
        }
    }
}