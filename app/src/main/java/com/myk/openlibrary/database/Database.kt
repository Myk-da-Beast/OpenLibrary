package com.myk.openlibrary.database

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.myk.openlibrary.listToTruncatedString
import com.myk.openlibrary.model.Book
import io.realm.*
import timber.log.Timber

interface Database {

    /**
     * Store Book objects in the database
     */
    suspend fun cacheBooks(books: List<Book>, inMemory: Boolean)

    /**
     * Queries for a book by id. Checks in memory first before
     * querying persisted memory.
     *
     * @return the queried book object or null
     */
    suspend fun getBook(id: Int): Book?

    /**
     * Queries for books from the database, and then listen for changes on the data
     */
    fun observeBooks(): LiveData<RealmResults<Book>>

    /**
     * Returns a query for all books from the in-memory realm. A query object allows us to listen
     * for changes.
     */
    fun getAllBooksQuery(): RealmQuery<Book>

    /**
     * Returns a query for wish list items. A query object allows us to listen
     * for changes.
     */
    fun getWishListQuery(): RealmQuery<Book>
}

class DatabaseImpl : Database {

    private val persistedRealmConfiguration = RealmConfiguration.Builder()
        .name("persisted-realm.realm")
        .deleteRealmIfMigrationNeeded()
        .build()

    init {
        Timber.d("Initializing Realm DB")

        // Sets up an in-memory realm as the default. This way we can store information in-memory to increase
        // performance without having to persist everything.
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .name("in-memory-realm.realm")
                .deleteRealmIfMigrationNeeded()
                .inMemory()
                .build()
        )
    }

    override fun observeBooks(): LiveData<RealmResults<Book>> =
        Realm.getDefaultInstance().where(Book::class.java).findAll().asLiveData()

    override suspend fun cacheBooks(books: List<Book>, inMemory: Boolean) {
        Timber.v("Caching ${books.size} books")
        safeExecute(inMemory) { realm ->
            books.forEach { book ->
                book.listToTruncatedString(book.authorName)?.let {
                    book.authors = it
                }
                book.listToTruncatedString(book.subject)?.let {
                    book.subjects = it
                }
                book.listToTruncatedString(book.publisher)?.let {
                    book.publishers = it
                }
                book.preview = TextUtils.join(" ", book.firstSentence)
            }
            realm.copyToRealmOrUpdate(books)
        }
    }

    override suspend fun getBook(id: Int): Book? {
        Timber.v("Querying book with id: $id")

        val inMemoryRealm = Realm.getDefaultInstance()
        val inMemoryQuery = inMemoryRealm.where(Book::class.java).equalTo("coverI", id)

        val persistedRealm = Realm.getInstance(persistedRealmConfiguration)
        val persistedQuery = persistedRealm.where(Book::class.java).equalTo("coverI", id)

        return safeFindFirst(inMemoryRealm, inMemoryQuery) ?: safeFindFirst(persistedRealm, persistedQuery)
    }

    override fun getAllBooksQuery(): RealmQuery<Book> = Realm.getDefaultInstance().where(Book::class.java)

    override fun getWishListQuery(): RealmQuery<Book> =
        Realm.getInstance(persistedRealmConfiguration).where(Book::class.java).equalTo("isOnWishList", true)

    // takes a transaction and handles the realm instance while performing that transaction
    private fun safeExecute(inMemory: Boolean = true, transaction: (Realm) -> Unit) {
        val realm = if (inMemory) Realm.getDefaultInstance() else Realm.getInstance(persistedRealmConfiguration)
        realm.executeTransaction(transaction)
        realm.close()
    }

    // queries an object and handles realm management
    private fun <T : RealmObject> safeFindFirst(realm: Realm, query: RealmQuery<T>): T? {
        val managedResult = query.findFirst() ?: return null

        // since we are closing realm anyway there is no reason to use a managed object here
        val unmanagedResult = realm.copyFromRealm(managedResult)
        realm.close()
        return unmanagedResult
    }
}