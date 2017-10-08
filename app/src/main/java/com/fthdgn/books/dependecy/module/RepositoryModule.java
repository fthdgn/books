package com.fthdgn.books.dependecy.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.fthdgn.books.repository.AppDatabase;
import com.fthdgn.books.repository.BookDao;

@Module
public class RepositoryModule {

    private static final String DATABASE_NAME = "app-database";

    @Provides
    @Singleton
    AppDatabase providesRoomDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    BookDao providesBookDao(AppDatabase appDatabase) {
        return appDatabase.bookDao();
    }
}
