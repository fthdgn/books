package tr.name.fatihdogan.books.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Book.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookDao bookDao();

}
