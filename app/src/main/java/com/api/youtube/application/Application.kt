package com.api.youtube.application

class Application : android.app.Application() {
    
    lateinit var applicationModel : ApplicationModel
    
    override fun onCreate() {
        super.onCreate()
        
        applicationModel = ApplicationModel.getInstance(this)
    }
}