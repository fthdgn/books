package tr.name.fatihdogan.books.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import tr.name.fatihdogan.books.BaseApplication;

@Database(entities = {Book.class, Split.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app-database";

    private static AppDatabase instance;

    public static void initialize(BaseApplication baseApplication) {
        instance = Room.databaseBuilder(baseApplication, AppDatabase.class, DATABASE_NAME).build();
    }

    public static BookDao getBookDao() {
        if (instance == null)
            throw new IllegalStateException("Database is not initialized.");
        return instance.bookDao();
    }

    public abstract BookDao bookDao();

}
