package com.fthdgn.books.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Book::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

}
