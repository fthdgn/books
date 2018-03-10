package com.fthdgn.books.dependecy.module

import android.app.Application
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {
    @Provides
    @Singleton
    internal fun provideSharedPreferences(application: Application): SharedPreferences =
            application.getSharedPreferences("MyPref", 0)
}
