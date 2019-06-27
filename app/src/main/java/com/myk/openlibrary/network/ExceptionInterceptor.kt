package com.myk.openlibrary.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

interface ExceptionInterceptor : Interceptor

// this class allows us to impose our own constraints on network requests
class ExceptionInterceptorImpl : ExceptionInterceptor {


    // intercept the request and check for error
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain) : Response {
        val response = chain.proceed(chain.request())

        if (response.code() >= 400) throw IOException(response.message())

        return response
    }
}