package com.api.youtube.api

import com.api.youtube.data.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    ///////////////////////////////////////////////////////////////////////////////////////////////
    @GET("search?")
    suspend fun search(@Query("part") part: String = "snippet",
                       @Query("order") order: String = "viewCount",
                       @Query("q") q: String,
                       @Query("type") type: String = "video",
                       @Query("maxResults") maxResults: Int = 20,
                       @Query("pageToken") pageToken: String? = null,
//                        @Query("videoDefinition") videoDefinition: String = "high",
                       @Query("key") key: String)
    : SearchResponse
    
    @GET("channels?")
    suspend fun history(@Query("part") part: String = "contentDetails",
                       @Query("mine") mine: Boolean = true,
                       @Query("key") key: String)
            : Any
}