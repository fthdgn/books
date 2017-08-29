package tr.name.fatihdogan.books.dependecy.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tr.name.fatihdogan.books.repository.AppDatabase;
import tr.name.fatihdogan.books.repository.BookDao;

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
