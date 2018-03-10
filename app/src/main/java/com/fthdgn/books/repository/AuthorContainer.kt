package com.fthdgn.books.repository

import android.arch.persistence.room.TypeConverters

class AuthorContainer {

    @TypeConverters(AuthorConverter::class)
    var authors: List<String>? = null
}
