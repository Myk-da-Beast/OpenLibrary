package com.myk.openlibrary.database

import androidx.lifecycle.LiveData
import com.myk.openlibrary.model.Book
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import timber.log.Timber

interface Database {

    /**
     * Store Book objects in the database
     */
    suspend fun cacheBooks(books: List<Book>)

    /**
     * Queries for books from the database, and then listen for changes on the data
     */
    fun observeBooks(): LiveData<RealmResults<Book>>
}

class DatabaseImpl: Database {

    private val persistedRealm = RealmConfiguration.Builder()
        .name("persisted-realm.realm")
        .deleteRealmIfMigrationNeeded()
        .build()

    init {
        Timber.d("Initializing Realm DB")

        // Sets up an in-memory realm as the default. This way we can store information in-memory to increase
        // performance without having to persist everything.
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
            .name("in-memory-realm.realm")
            .deleteRealmIfMigrationNeeded()
            .inMemory()
            .build())
    }

    override fun observeBooks(): LiveData<RealmResults<Book>> =
        Realm.getDefaultInstance().where(Book::class.java).findAll().asLiveData()

    override suspend fun cacheBooks(books: List<Book>) {
        Timber.v("Caching ${books.size} books")
        safeExecute {
            it.copyToRealmOrUpdate(books)
        }
    }

    // takes a transaction and handles the realm instance while performing that transaction
    private fun safeExecute(inMemory: Boolean = true, transaction: (Realm) -> Unit) {
        val realm = if (inMemory) Realm.getDefaultInstance() else Realm.getInstance(persistedRealm)
        realm.executeTransaction(transaction)
        realm.close()
    }
}