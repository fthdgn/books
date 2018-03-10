package com.fthdgn.books.repository

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters

@Entity
class Book(@field:PrimaryKey
           val bookId: String) {

    var title: String? = null

    var sortTitle: String? = null

    @TypeConverters(AuthorConverter::class)
    var authors: List<String>? = null

    var originalTitle: String? = null

    @TypeConverters(AuthorConverter::class)
    var originalAuthors: List<String>? = null

    var formattedAuthors: String
        get() {
            val r = stringArrayToFormattedString(authors)
            return r ?: ""
        }
        set(formattedAuthors) {
            authors = formattedStringToStringArray(formattedAuthors)
        }

    val formattedOriginalAuthors: String
        get() {
            val r = stringArrayToFormattedString(originalAuthors)
            return r ?: ""
        }

    private fun stringArrayToFormattedString(array: List<String>?): String? =
            when {
                array == null -> null
                array.size == 1 -> array[0]
                else -> {
                    val s = StringBuilder(array[0])
                    for (i in 1 until array.size)
                        s.append(", ").append(array[i])
                    s.toString()
                }
            }

    private fun formattedStringToStringArray(string: String?): List<String>? {
        if (string == null)
            return null

        return string.split(",".toRegex())
                .map { t -> t.trim { it <= ' ' } }
                .filter { it != "" }
        //TODO Be sure never returns array with length 0
    }

}
