package com.fthdgn.books.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {

    private static BookDao bookDao;
    private static List<Book> initialBooks;

    @BeforeClass
    public static void set() {
        Context context = InstrumentationRegistry.getTargetContext();
        AppDatabase database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        bookDao = database.bookDao();

        initialBooks = new ArrayList<>();
        initialBooks.add(newBook("1", "Title1", "SortTitle1", "A,B", "Title1", "A,D"));
        initialBooks.add(newBook("4", "Title4", "SortTitle4", "A", "Title4", "A"));
        initialBooks.add(newBook("3", "Title3", "SortTitle3", "C", "Title3", "C"));
        initialBooks.add(newBook("2", "Title2", "SortTitle2", "B", "Title2", "D"));
    }

    @Before
    public void clearDatabase() {
        bookDao.deleteAll();
        bookDao.insertAll(initialBooks.toArray(new Book[initialBooks.size()]));
    }

    @Test
    public void getAllTest() {
        List<Book> books = bookDao.getAll();
        assertTrue(books.size() == initialBooks.size());
        for (Book book : initialBooks)
            assertTrue(books.contains(book));
    }

    @Test
    public void getAllSortedTest() {
        List<Book> books = bookDao.getAllSorted();
        Iterator<Book> iterator = books.iterator();

        Book first = iterator.next();

        while (iterator.hasNext()) {
            Book second = iterator.next();
            assertFalse(first.getSortTitle().compareTo(second.getSortTitle()) > 0);
            first = second;
        }
    }

    @Test
    public void getByIdTest() {
        Book book = bookDao.getById(initialBooks.get(0).getBookId());
        assertTrue(book.equals(initialBooks.get(0)));
    }

    @Test
    public void getByIdLiveTest() {
        final LiveData<Book> liveData = bookDao.getByIdLive(initialBooks.get(0).getBookId());
        liveData.observeForever(new Observer<Book>() {
            @Override
            public void onChanged(@Nullable Book book) {
                assertNotNull(book);
                assertTrue(book.equals(initialBooks.get(0)));
                liveData.removeObserver(this);
            }
        });
    }

    @Test
    public void getByAuthorTest() {
        String author = "A";
        List<Book> books = bookDao.getByAuthor(author);

        for (Book b : initialBooks) {
            if (b.getAuthors().contains(author)) {
                assertTrue(books.contains(b));
            } else {
                assertFalse(books.contains(b));
            }
        }
    }

    @Test
    public void getAllAuthorsTest() {
        final Set<String> set = new HashSet<>();
        for (Book b : initialBooks) {
            set.addAll(b.getAuthors());
        }

        List<String> strings = bookDao.getAllAuthors();
        assertNotNull(strings);
        assertTrue(set.size() == strings.size());
        for (String aSet : set) {
            assertTrue(strings.contains(aSet));
        }
    }

    @Test
    public void getAllAuthorsLiveTest() {
        final Set<String> set = new HashSet<>();
        for (Book b : initialBooks) {
            set.addAll(b.getAuthors());
        }

        final LiveData<List<String>> liveData = bookDao.getAllAuthorsLive();
        liveData.observeForever(new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                assertNotNull(strings);
                assertTrue(set.size() == strings.size());

                for (String aSet : set) {
                    assertTrue(strings.contains(aSet));
                }
                liveData.removeObserver(this);
            }
        });
    }

    @Test
    public void renameAuthorTest() {
        String oldName = "A";
        String newName = "Z";
        bookDao.renameAuthor(oldName, newName);

        List<Book> oldList = bookDao.getByAuthor(oldName);
        assertTrue(oldList.size() == 0);

        List<Book> newList = bookDao.getByAuthor(newName);

        for (Book b : initialBooks) {
            if (b.getAuthors().contains(oldName) || b.getAuthors().contains(newName)) {
                boolean found = false;
                for (Book n : newList) {
                    if (n.getBookId().equals(b.getBookId())) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        }
    }

    @Test
    public void deleteOtherThanTheseIdsTest() {
        int count = initialBooks.size() / 2;
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(initialBooks.get(i).getBookId());
        }

        bookDao.deleteOtherThanTheseIds(ids.toArray(new String[count]));

        List<Book> books = bookDao.getAll();

        for (int i = 0; i < initialBooks.size(); i++) {
            if (i < count) {
                assertTrue(books.contains(initialBooks.get(i)));
            } else {
                assertFalse(books.contains(initialBooks.get(i)));
            }
        }
    }

    @Test
    public void deleteAllTest() {
        bookDao.deleteAll();
        List<Book> books = bookDao.getAll();
        assertTrue(books.size() == 0);
    }

    public static Book newBook(String id, String title, String sortTitle,
                               String authors, String originalTitle, String originalAuthors) {
        Book book = new Book(id);
        book.setTitle(title);
        book.setSortTitle(sortTitle);
        book.setOriginalTitle(originalTitle);
        List<String> authorsList = Arrays.asList(authors.split(","));
        book.setAuthors(authorsList);
        List<String> originalAuthorsList = Arrays.asList(originalAuthors.split(","));
        book.setOriginalAuthors(originalAuthorsList);
        return book;
    }

}
