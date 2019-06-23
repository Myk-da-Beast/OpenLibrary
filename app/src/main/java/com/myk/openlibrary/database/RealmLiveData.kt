package com.myk.openlibrary.database

import io.realm.RealmResults
import io.realm.RealmChangeListener
import androidx.lifecycle.LiveData
import io.realm.RealmModel

// Wrapper class to allow us to represent RealmResults as LiveData
class RealmLiveData<T>(private val results: RealmResults<T>) : LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }

    override fun onActive() {
        results.addChangeListener(listener)
    }

    override fun onInactive() {
        results.removeChangeListener(listener)
    }
}

// convenience function
fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)