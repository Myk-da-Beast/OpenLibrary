package com.myk.openlibrary.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.repository.BookRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookViewModel(
    private val repository: BookRepository
) : ViewModel() {

    val book: LiveData<Book>
        get() = _book

    private var _book = MutableLiveData<Book>()

    fun setBook(id: Int) {
        // TODO see if this can be refactored
        GlobalScope.launch {
            _book.postValue(repository.getBook(id))
        }
    }
}