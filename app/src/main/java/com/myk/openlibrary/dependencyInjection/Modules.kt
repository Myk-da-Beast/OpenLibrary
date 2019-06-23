package com.myk.openlibrary.dependencyInjection

import com.myk.openlibrary.database.Database
import com.myk.openlibrary.database.DatabaseImpl
import com.myk.openlibrary.network.*
import com.myk.openlibrary.repository.BookRepository
import com.myk.openlibrary.repository.BookRepositoryImpl
import com.myk.openlibrary.viewModel.SearchViewModel
import com.myk.openlibrary.viewModel.WishListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    // singleton dependencies
    single(createOnStart = true) { OpenLibraryApiService() }
    single<BooksDataSource>(createOnStart = true) { BooksDataSourceImpl(get()) }
    single<Database>(createOnStart = true) { DatabaseImpl() }

    // instanced dependencies
    factory<ConnectivityInterceptor> { ConnectivityInterceptorImpl(androidContext()) }
    factory<BookRepository> { BookRepositoryImpl(get(), get()) }
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