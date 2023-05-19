package com.api.youtube.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Client {
    private var retrofit: Retrofit? = null
    const val server = "https://youtube.googleapis.com/youtube/v3/"
    private const val REQUEST_TIMEOUT = 15000
    val client: Retrofit
        get() {
            initOkHttp()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(server)
                    .client(initOkHttp())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().serializeNulls()
                                .create()
                        )
                    )
                    .build()
            }
            return retrofit!!
        }
    
    private fun initOkHttp(): OkHttpClient {
        val okHttpClient: OkHttpClient
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        val interceptor = Interceptor { chain: Interceptor.Chain ->
            var request = chain.request()
            var requestBuilder = request.headers().newBuilder()
            requestBuilder = requestBuilder
                .add("Accept", "application/json")
                .add("Content-Type", "application/json")

//            requestBuilder.add("Authorization","Bearer " + "Application.getApplicationModel().getToken()");
            val headers = requestBuilder.build()
            request = request.newBuilder().headers(headers).build()
            chain.proceed(request)
        }
        httpClient.addInterceptor(interceptor)
        okHttpClient = httpClient.build()
        return okHttpClient
    }
}