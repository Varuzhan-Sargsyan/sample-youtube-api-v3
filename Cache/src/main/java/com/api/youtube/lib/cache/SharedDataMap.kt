package com.api.youtube.lib.cache

import android.content.SharedPreferences

enum class SharedDataMapKey {
    YOUTUBE_KEY
}

class SharedDataMap<Type: Enum<Type>>(sharedPreferences: SharedPreferences) {
    private val map = mutableMapOf<Type, SharedData<Type, *>>()
    private val localMemory = LocalMemory(sharedPreferences)
    
    fun <Value : Any> set(key: Type, data: Value?) : Boolean {
        if (localMemory.save(key, data)) {
            val sharedData : SharedData<Type, Value?> = if (map.containsKey(key)) {
                map[key] as SharedData<Type, Value?>
            } else
                SharedData<Type, Value?>(key, data)
            sharedData.set(data)
            map[key] = sharedData
            return true
        }
        return false
    }

    fun <Value: Any> load(key: Type, defaultValue: Value) {
        if (!localMemory.contains(key.toString()))
            localMemory.save(key, defaultValue)
        
        set(key, localMemory.load(key, defaultValue::class.java))
    }
    
    fun <Value> get(key: Type) : Value? {
        map[key]?.let {
            return it.value() as Value?
        } ?: return null
    }
    
    fun contains(key: Type) : Boolean {
        return map.containsKey(key)
    }

    fun clear() {
        map.clear()
    }
    
    fun <Value : Any> subscribe(key: Type, sharedCallback: SharedDataCallback1<Type, Value?>) {
        val sharedData: SharedData<Type, Value?> = map[key] as SharedData<Type, Value?>
        sharedData.subscribe(sharedCallback)
    }
    
    fun <Value : Any> unsubscribe(key: Type, sharedCallback: SharedDataCallback1<Type, Value?>) {
        val sharedData: SharedData<Type, Value?> = map[key] as SharedData<Type, Value?>
        sharedData.unsubscribe(sharedCallback)
    }
}