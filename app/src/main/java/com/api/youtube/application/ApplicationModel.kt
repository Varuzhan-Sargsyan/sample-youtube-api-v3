package com.api.youtube.application

import android.content.Context
import com.api.youtube.R
import com.api.youtube.lib.cache.SharedDataMap
import com.api.youtube.lib.cache.SharedDataMapKey

open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null
    
    fun getInstance() : T = synchronized(this) {
        instance!!
    }
    
    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }
        
        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

class ApplicationModel private constructor(private val application: Application) {
    
    companion object : SingletonHolder<ApplicationModel, Application>(::ApplicationModel)
    
    val sharedDataMap by lazy {
        SharedDataMap<SharedDataMapKey>(application.getSharedPreferences("ApplicationModel.dat", Context.MODE_PRIVATE))
            .apply {
                set(SharedDataMapKey.YOUTUBE_KEY, getGoogleApiKey())
            }
    }
    
    fun getGoogleApiKey() : String {
        return application.resources.getString(R.string.google_api_key)
    }
}