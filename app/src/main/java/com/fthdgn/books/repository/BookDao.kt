package com.fthdgn.books.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import java.util.*

@Dao
interface BookDao {

    @get:Query(GET_ALL)
    val all: List<Book>

    @get:Query(GET_ALL_SORTED)
    val allSorted: List<Book>

    @get:Query(GET_ALL_SORTED)
    val allSortedLive: LiveData<List<Book>>

    @get:Query("SELECT authors FROM book")
    val authors: List<AuthorContainer>

    @get:Query("SELECT authors FROM book")
    val authorsLive: LiveData<List<AuthorContainer>>

    @Query(GET_BY_ID)
    fun getById(id: String): Book

    @Query(GET_BY_ID)
    fun getByIdLive(id: String): LiveData<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg books: Book)

    @Query(GET_BY_AUTHOR)
    fun getByAuthor(author: String): List<Book>

    @Query(GET_BY_AUTHOR)
    fun getByAuthorLive(author: String): LiveData<List<Book>>

    @Query("" +
            " UPDATE book " +
            " SET authors = replace (authors, '|'||:oldAuthor||'|', '|'||:newAuthor||'|' ) " +
            " WHERE authors LIKE '%|'||:oldAuthor||'|%'")
    fun renameAuthor(oldAuthor: String, newAuthor: String)

    @Query("DELETE FROM book WHERE bookId NOT IN (:bookIds)")
    fun deleteOtherThanTheseIds(vararg bookIds: String)

    @Query("DELETE FROM book")
    fun deleteAll()


    companion object {


        const val GET_BY_ID = "SELECT * FROM book WHERE bookId = :id"

        const val GET_ALL = "SELECT * FROM book"

        const val GET_ALL_SORTED = GET_ALL + " ORDER BY sortTitle COLLATE NOCASE ASC"

        const val GET_BY_AUTHOR = "SELECT * FROM book WHERE authors LIKE '%|'||:author||'|%'"

        internal fun flattenAuthors(authorContainers: List<AuthorContainer>): List<String> {
            val sortedSet = TreeSet<String>()
            for (authorContainer in authorContainers)
                sortedSet.addAll(authorContainer.authors!!)
            return ArrayList(sortedSet)
        }
    }
}

fun BookDao.getAllAuthors(): List<String> = BookDao.flattenAuthors(this.authors)

fun BookDao.getAllAuthorsLive(): LiveData<List<String>> {
    return Transformations.map(this.authorsLive, {
        BookDao.flattenAuthors(it)
    })
}

