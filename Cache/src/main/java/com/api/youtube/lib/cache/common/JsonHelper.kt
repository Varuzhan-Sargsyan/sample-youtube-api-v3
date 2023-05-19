package com.api.youtube.lib.cache.common

import com.google.gson.Gson

class JsonHelper {
    
    companion object {
        fun <T> fromJson(json: String?, type: Class<T>) : T? {
            return json?.let { return gson.fromJson(json, type) } ?: return null
        }
        
        // create a gson object and use it to convert the json string to the object
        fun <T> toJson(value: T) : String? {
            return gson.toJson(value)
        }
        
        private val gson = Gson().newBuilder().serializeNulls().create()
        
    }
    
}