package com.fthdgn.books.api;

import android.support.annotation.IntRange;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.fthdgn.books.api.response.Volumes;

public interface GoogleBooksApi {

    @GET("mylibrary/bookshelves/7/volumes")
    Call<Volumes> getMyVolumes(@Query("startIndex") @IntRange(from = 0) int startIndex);

}
