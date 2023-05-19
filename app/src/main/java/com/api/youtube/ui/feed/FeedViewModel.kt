package com.api.youtube.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.youtube.application.ApplicationModel
import com.api.youtube.data.SearchItem
import com.api.youtube.lib.cache.SharedDataMapKey
import com.api.youtube.services.YoutubeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedViewModel : ViewModel() {
    
    private val applicationModel by lazy { ApplicationModel.getInstance() }
    private val youtubeService by lazy { YoutubeService(applicationModel.getGoogleApiKey()) }
    
    private var keys = ""
    // Expose screen UI state
    val searchResult = MutableLiveData<MutableList<SearchItem>>(mutableListOf())
    val loading = MutableLiveData(false)
    val error = MutableStateFlow("")
    
    fun reload() {
        search()
    }
    
    fun downloadNextPage() {
        search(keys)
    }
    
    // Handle business logic
    fun search(keys: String? = null) {
        loading.postValue(true)
        
        val reload = keys == null || this.keys != keys
        if (reload) {
            this.keys = keys ?: ""
            searchResult.postValue(mutableListOf())
        }
        
        viewModelScope.launch {
            try {
                youtubeService.search(keys ?: "", !reload)?.let { result ->
                    with(searchResult) {
                        postValue(value?.plus(result)?.toMutableList())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                error.update { e.message ?: "" }
            }
            loading.postValue(false)
        }
    }
    
    fun hasNextPage(): Boolean {
        return youtubeService.hasNextPage()
    }
}