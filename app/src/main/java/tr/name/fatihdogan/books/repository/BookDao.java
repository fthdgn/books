package tr.name.fatihdogan.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

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

    /*
    Source:
    http://sqlite.1065341.n5.nabble.com/comma-separated-string-data-tp74926p74941.html
     */
    String GET_ALL_AUTHORS = "" +
            "   WITH split(len,c,r) AS ( " +
            "       SELECT 1, authors, '' FROM book " +
            "     UNION ALL " +
            "       SELECT instr(c,'|') AS vLen, " +
            "              substr(c,instr(c,'|')+1), " +
            "              substr(c,1,instr(c,'|')-1) " +
            "       FROM split " +
            "       WHERE vLen>0 " +
            "     ) " +
            "   SELECT DISTINCT split.r FROM split " +
            "   WHERE split.r<>'' " +
            "   ORDER BY split.r";

    @Query(GET_ALL_AUTHORS)
    List<String> getAllAuthors();

    @Query(GET_ALL_AUTHORS)
    LiveData<List<String>> getAllAuthorsLive();

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
