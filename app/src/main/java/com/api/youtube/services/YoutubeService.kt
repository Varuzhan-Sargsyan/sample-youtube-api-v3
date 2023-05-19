package com.api.youtube.services

import com.api.youtube.api.Api
import com.api.youtube.api.Client
import com.api.youtube.data.SearchItem

class YoutubeService(private val gooleKey: String) {
    private var nextPageToken: String? = null
    private var api: Api = Client.client.create(Api::class.java)
    
    suspend fun search(keys: String = "", nextPage: Boolean = false) : List<SearchItem>? {
        return if (nextPage) nextPage(keys) else startSearch(keys)
    }
    
    private suspend fun startSearch(keys: String) : List<SearchItem>? {
        nextPageToken = null
        val response = api.search(
            q = keys,
            key = gooleKey)
        nextPageToken = response.nextPageToken
        return response.items
    }
    
    private suspend fun nextPage(keys: String) : List<SearchItem>? {
        val response = api.search(
            q = keys,
            pageToken = nextPageToken,
            key = gooleKey)
        nextPageToken = response.nextPageToken
        return response.items
    }
    
    fun hasNextPage() = nextPageToken != null
    
    suspend fun downloadHistory() {
        val ret = api.history(key = gooleKey)
        println(ret)
    }
}