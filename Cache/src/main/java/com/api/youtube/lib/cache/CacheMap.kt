package com.api.youtube.lib.cache

//import android.content.SharedPreferences
//import com.api.youtube.lib.cache.common.MemoryInterface

//abstract class CacheMap(private val memoryInterface: MemoryInterface? = null) {
//
//    fun <T : Any> set(sharedData: SharedData<T?>) : Boolean {
//        return memoryInterface?.set(key, value)
//    }
//
//    fun <T : Any> get(key: String, type: Class<T>) : T? {
//        if (key.isEmpty() || !memoryInterface.contains(key)) {
//            return null
//        }
//
//        return memoryInterface.get(key, type)
//    }
//
//    fun clear() {
//        memoryInterface.clear()
//    }
//
//}

//class CacheDataInLocalMemory(sharedPreferences: SharedPreferences) : CacheMap(LocalMemory(sharedPreferences))