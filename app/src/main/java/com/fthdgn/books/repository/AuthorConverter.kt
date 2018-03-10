package com.fthdgn.books.repository

import android.arch.persistence.room.TypeConverter
import java.util.*

class AuthorConverter {

    @TypeConverter
    fun authorsFromString(value: String): List<String> {
        val values = value.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val authors = ArrayList<String>(values.size)
        values.filterTo(authors) { it.isNotEmpty() }
        return authors
    }

    @TypeConverter
    fun authorsToString(values: List<String>?): String {
        if (values == null)
            return ""
        val stringBuilder = StringBuilder()
        for (value in values) {
            stringBuilder.append("|").append(value).append("|")
        }
        return stringBuilder.toString()
    }

}
