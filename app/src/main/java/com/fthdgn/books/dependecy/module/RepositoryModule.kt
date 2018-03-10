package com.fthdgn.books.dependecy.module

import android.app.Application
import android.arch.persistence.room.Room
import com.fthdgn.books.repository.AppDatabase
import com.fthdgn.books.repository.BookDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun providesRoomDatabase(application: Application): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, "getApplication-database").build()

    @Provides
    @Singleton
    internal fun providesBookDao(appDatabase: AppDatabase): BookDao =
            appDatabase.bookDao()

}
