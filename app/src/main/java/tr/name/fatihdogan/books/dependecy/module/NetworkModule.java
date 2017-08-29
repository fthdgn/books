package tr.name.fatihdogan.books.dependecy.module;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tr.name.fatihdogan.books.api.AccountManager;
import tr.name.fatihdogan.books.api.GoogleBooksApi;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(AccountManager accountManager, Cache cache) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder().addQueryParameter("access_token", accountManager.getToken()).build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        }).cache(cache).build();
    }

    @Provides
    @Singleton
    GoogleBooksApi providesGoogleBooksApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GoogleBooksApi.class);
    }

    @Provides
    @Singleton
    Cache providesCache(Application application) {
        return new Cache(new File(application.getCacheDir(), "okHttpCache"),
                1024 * 1024 * 100);
    }

    @Provides
    @Singleton
    Picasso providesPicasso(Application application, Downloader downloader) {
        return new Picasso.Builder(application).downloader(downloader).build();
    }

    @Provides
    @Singleton
    Downloader downloader(OkHttpClient client) {
        return new OkHttp3Downloader(client);
    }

}
