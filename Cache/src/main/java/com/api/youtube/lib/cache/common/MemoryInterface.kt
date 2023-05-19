package com.api.youtube.lib.cache.common

import com.api.youtube.lib.cache.SharedData

interface MemoryInterface {
    fun <D : Enum<D>, T> save(key: D, data: T?) : Boolean
    fun <D : Enum<D>, T> load(key: D, type: Class<T>) : T?
    fun contains(key: String) : Boolean
    fun clear()
}