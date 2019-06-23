package com.myk.openlibrary.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.myk.openlibrary.model.OpenLibraryResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// http://openlibrary.org/search.json?q=the+lord+of+the+rings
// http://openlibrary.org/search.json?title=the+lord+of+the+rings
// http://openlibrary.org/search.json?author=tolkien
// http://openlibrary.org/search.json?q=the+lord+of+the+rings&page=2
interface OpenLibraryApiService {
    @GET("search.json")
    fun searchForBookAsync(
        @Query("q") query: String,
        @Query("page") page: Int = 1
    ): Deferred<OpenLibraryResponse>

    // the KoinComponent interface allows us to use dependency injection
    companion object: KoinComponent {
        private val connectivityInterceptor: ConnectivityInterceptor by inject()

        operator fun invoke(): OpenLibraryApiService = Retrofit.Builder()
            .client(OkHttpClient().newBuilder().addInterceptor(connectivityInterceptor).build())
            .baseUrl("https://openlibrary.org/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenLibraryApiService::class.java)
    }
}