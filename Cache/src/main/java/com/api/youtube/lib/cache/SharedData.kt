package com.api.youtube.lib.cache

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

//interface SharedDataStateCallback<Type: Enum<Type>, Value> {
//    fun onUpdate(key: Type, data: Value?)
//}

typealias SharedDataCallback1<Type, Value> = (type: Type, data: Value) -> Unit

open class SharedData<Type: Enum<Type>, Value : Any?>(
    private var key: Type,
    private var value: Value? = null,
    private val observeOnScheduler: Scheduler = AndroidSchedulers.mainThread()
) {
    
    private val listObservers = mutableListOf<SharedDataCallback1<Type, Value>>()
    
    fun subscribe(sharedCallback: SharedDataCallback1<Type, Value?>) {
        listObservers.add(sharedCallback)
        
        sharedCallback(key, value)
    }
    
    fun unsubscribe(observer: SharedDataCallback1<Type, Value>) {
        listObservers.remove(observer)
    }
    
    fun unregisterAll() {
        listObservers.clear()
    }
    
    fun set(value: Value) {
        this.value = value
        
        if (listObservers.size == 0)
            return
    
        val disposable = CompositeDisposable()
        disposable.add(
            Observable
                .range(0, listObservers.size)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { listObservers[it](key, value) },
                    { throw it },
                    { disposable.dispose() }
                )
        )
    }
    
    fun value() : Value? = value
    fun key() = key
}