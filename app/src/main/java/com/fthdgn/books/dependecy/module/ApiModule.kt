package com.fthdgn.books.dependecy.module

import android.content.SharedPreferences
import com.fthdgn.books.api.AccountManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    internal fun providesAccountManager(sharedPreferences: SharedPreferences): AccountManager =
            AccountManager(sharedPreferences)

}
