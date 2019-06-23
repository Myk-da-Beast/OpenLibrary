package com.myk.openlibrary.dependencyInjection

import com.myk.openlibrary.network.*
import com.myk.openlibrary.repository.BookRepository
import com.myk.openlibrary.repository.BookRepositoryImpl
import com.myk.openlibrary.search.SearchViewModel
import com.myk.openlibrary.wishList.WishListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    // singleton dependencies
    single(createOnStart = true) { OpenLibraryApi() }
    single<BooksDataSource>(createOnStart = true) { BooksDataSourceImpl(get()) }

    // instanced dependencies
    factory<ConnectivityInterceptor> { ConnectivityInterceptorImpl(androidContext()) }
}

// MARK: view model modules

val searchModule = module {
    viewModel { SearchViewModel() }
}

val wishListModule = module {
    viewModel { WishListViewModel() }
}

// all modules to include
val modules = listOf(appModule, searchModule, wishListModule)