package com.fthdgn.books.dependecy.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.fthdgn.books.api.AccountManager;

@Module
public class ApiModule {

    @Provides
    @Singleton
    AccountManager providesAccountManager(SharedPreferences sharedPreferences) {
        return new AccountManager(sharedPreferences);
    }

}
