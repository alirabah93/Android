package com.smartherd.globofly.services

import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    private const val URL = "http://10.0.2.2:9000/"

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

//    Create a Custom Interceptor to apply Headers application wide
    val headerInterceptor : Interceptor = object: Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {

            var request: Request = chain.request()

            request = request.newBuilder()
                .addHeader("x-device-type", Build.DEVICE)
                .addHeader("Accept-Language", Locale.getDefault().language)
                .build()

            return chain.proceed(request)
        }
    }

//    Create okHttp Client
    private val okHttp : OkHttpClient.Builder = OkHttpClient.Builder()
                                                .callTimeout(5, TimeUnit.SECONDS)
                                                .addInterceptor(headerInterceptor)
                                                .addInterceptor(logger)

//    Create Retrofit Builder
    private val builder : Retrofit.Builder = Retrofit.Builder().baseUrl(URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .client(okHttp.build())

//    Create Retrofit Instance
    private val retrofit : Retrofit = builder.build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}