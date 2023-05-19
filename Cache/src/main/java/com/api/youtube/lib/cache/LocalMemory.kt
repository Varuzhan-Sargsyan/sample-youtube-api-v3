package com.api.youtube.lib.cache

import android.content.SharedPreferences
import com.api.youtube.lib.cache.common.JsonHelper
import com.api.youtube.lib.cache.common.MemoryInterface

class LocalMemory(private val sharedPreferences: SharedPreferences) : MemoryInterface {

//    override fun <T> create(key: String, type: Class<T>, defaultValue: T?): Boolean {
//        sharedPreferences.edit().putString(key, JsonHelper.toJson(defaultValue)).apply()
//        return true
//    }
    
    override fun <D : Enum<D>, T> save(key: D, data: T?): Boolean {
        sharedPreferences.edit().putString(key.toString(), JsonHelper.toJson(data)).apply()
        return true
    }
    
    override fun <D : Enum<D>, T> load(key: D, type: Class<T>) : T? {
        return if (contains(key.toString()))
            JsonHelper.fromJson(sharedPreferences.getString(key.toString(), null), type)
        else
            null
    }
    
    
    override fun contains(key: String): Boolean {
        sharedPreferences.getString(key, null)?.let {
            return true
        } ?: return false
    }
    
    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
    
}