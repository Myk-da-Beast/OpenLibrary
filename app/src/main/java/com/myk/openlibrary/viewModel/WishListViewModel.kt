package com.myk.openlibrary.viewModel

import androidx.lifecycle.ViewModel
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.repository.BookRepository
import io.realm.RealmQuery

class WishListViewModel(
    private val repository: BookRepository
) : ViewModel() {
    fun getWishlistQuery(): RealmQuery<Book> = repository.getWishListQuery()
}