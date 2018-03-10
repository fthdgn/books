package com.fthdgn.books.dependecy.module

import android.app.Application
import com.fthdgn.books.api.AccountManager
import com.fthdgn.books.api.GoogleBooksApi
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun providesOkHttpClient(accountManager: AccountManager, cache: Cache): OkHttpClient =
            OkHttpClient.Builder().addInterceptor { chain ->
                var request = chain.request()
                val url = request.url().newBuilder().addQueryParameter("access_token", accountManager.token).build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }.cache(cache).build()

    @Provides
    @Singleton
    internal fun providesGoogleBooksApi(client: OkHttpClient): GoogleBooksApi
            = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)

    @Provides
    @Singleton
    internal fun providesCache(application: Application): Cache =
            Cache(File(application.cacheDir, "okHttpCache"), (1024 * 1024 * 100).toLong())

    @Provides
    @Singleton
    internal fun providesPicasso(application: Application, downloader: Downloader): Picasso =
            Picasso.Builder(application).downloader(downloader).build()

    @Provides
    @Singleton
    internal fun downloader(client: OkHttpClient): Downloader =
            OkHttp3Downloader(client)

}
