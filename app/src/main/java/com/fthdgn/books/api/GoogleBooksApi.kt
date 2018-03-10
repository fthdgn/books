package com.fthdgn.books.api

import android.support.annotation.IntRange
import com.fthdgn.books.api.response.Volumes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("mylibrary/bookshelves/7/volumes")
    fun getMyVolumes(@Query("startIndex") @IntRange(from = 0) startIndex: Int): Call<Volumes>

}
