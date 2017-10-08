package com.fthdgn.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@SuppressWarnings("WeakerAccess")
@Dao
public interface BookDao {

    String GET_BY_ID = "SELECT * FROM book WHERE bookId = :id";

    @Query(GET_BY_ID)
    Book getById(String id);

    @Query(GET_BY_ID)
    LiveData<Book> getByIdLive(String id);

    String GET_ALL = "SELECT * FROM book";

    @Query(GET_ALL)
    List<Book> getAll();

    String GET_ALL_SORTED = GET_ALL + " ORDER BY sortTitle COLLATE NOCASE ASC";

    @Query(GET_ALL_SORTED)
    List<Book> getAllSorted();

    @Query(GET_ALL_SORTED)
    LiveData<List<Book>> getAllSortedLive();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Book... books);

    String GET_BY_AUTHOR = "SELECT * FROM book WHERE authors LIKE '%|'||:author||'|%'";

    @Query(GET_BY_AUTHOR)
    List<Book> getByAuthor(String author);

    @Query(GET_BY_AUTHOR)
    LiveData<List<Book>> getByAuthorLive(String author);

    @Query("SELECT authors FROM book")
    List<AuthorContainer> getAuthors();

    @Query("SELECT authors FROM book")
    LiveData<List<AuthorContainer>> getAuthorsLive();

    static List<String> flattenAuthors(List<AuthorContainer> authorContainers) {
        SortedSet<String> sortedSet = new TreeSet<>();
        for (AuthorContainer authorContainer : authorContainers)
            sortedSet.addAll(authorContainer.getAuthors());
        return new ArrayList<>(sortedSet);
    }

    default List<String> getAllAuthors() {
        return flattenAuthors(getAuthors());
    }

    default LiveData<List<String>> getAllAuthorsLive() {
        return Transformations.map(getAuthorsLive(), BookDao::flattenAuthors);
    }

    @Query("" +
            " UPDATE book " +
            " SET authors = replace (authors, '|'||:oldAuthor||'|', '|'||:newAuthor||'|' ) " +
            " WHERE authors LIKE '%|'||:oldAuthor||'|%'")
    void renameAuthor(String oldAuthor, String newAuthor);

    @Query("DELETE FROM book WHERE bookId NOT IN (:bookIds)")
    void deleteOtherThanTheseIds(String... bookIds);

    @Query("DELETE FROM book")
    void deleteAll();
}
