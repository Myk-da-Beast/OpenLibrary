package com.myk.openlibrary

import com.myk.openlibrary.database.Database
import com.myk.openlibrary.database.DatabaseImpl
import com.myk.openlibrary.network.*
import com.myk.openlibrary.repository.BookRepository
import com.myk.openlibrary.repository.BookRepositoryImpl
import com.myk.openlibrary.viewModel.BookViewModel
import com.myk.openlibrary.viewModel.SearchViewModel
import com.myk.openlibrary.viewModel.WishListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    // singleton dependencies
    single(createOnStart = true) { OpenLibraryApiService() }
    single<Database>(createOnStart = true) { DatabaseImpl() }

    // instanced dependencies
    factory<ConnectivityInterceptor> { ConnectivityInterceptorImpl(androidContext()) }
    factory<ExceptionInterceptor> { ExceptionInterceptorImpl() }
    factory<BookRepository> { BookRepositoryImpl(get(), get()) }
}

// MARK: view model modules

val searchModule = module {
    viewModel { SearchViewModel(androidContext(), get()) }
}

val detailsModule = module {
    viewModel { BookViewModel(get()) }
}

val wishListModule = module {
    viewModel { WishListViewModel(get()) }
}

// all modules to include
val modules = listOf(
    appModule,
    searchModule,
    detailsModule,
    wishListModule
)