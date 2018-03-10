package com.fthdgn.books.repository

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    @Before
    fun clearDatabase() {
        bookDao!!.deleteAll()
        bookDao!!.insertAll(*initialBooks!!.toTypedArray())
    }

    @Test
    fun getAllTest() {
        val books = bookDao!!.all
        assertTrue(books.size == initialBooks!!.size)
        for (book in initialBooks!!)
            assertTrue(books.contains(book))
    }

    @Test
    fun getAllSortedTest() {
        val books = bookDao!!.allSorted
        val iterator = books.iterator()

        var first = iterator.next()

        while (iterator.hasNext()) {
            val second = iterator.next()
            assertFalse(first.sortTitle!!.compareTo(second.sortTitle!!) > 0)
            first = second
        }
    }

    @Test
    fun getByIdTest() {
        val book = bookDao!!.getById(initialBooks!![0].bookId)
        assertTrue(book == initialBooks!![0])
    }

    @Test
    fun getByIdLiveTest() {
        val liveData = bookDao!!.getByIdLive(initialBooks!![0].bookId)
        liveData.observeForever(object : Observer<Book> {
            override fun onChanged(book: Book?) {
                assertNotNull(book)
                assertTrue(book == initialBooks!![0])
                liveData.removeObserver(this)
            }
        })
    }

    @Test
    fun getByAuthorTest() {
        val author = "A"
        val books = bookDao!!.getByAuthor(author)

        for (b in initialBooks!!) {
            if (b.authors!!.contains(author)) {
                assertTrue(books.contains(b))
            } else {
                assertFalse(books.contains(b))
            }
        }
    }

    @Test
    fun getAllAuthorsTest() {
        val set = HashSet<String>()
        for (b in initialBooks!!) {
            set.addAll(b.authors!!)
        }

        val strings = bookDao!!.getAllAuthors()
        assertNotNull(strings)
        assertTrue(set.size == strings.size)
        for (aSet in set) {
            assertTrue(strings.contains(aSet))
        }
    }

    @Test
    fun getAllAuthorsLiveTest() {
        val set = HashSet<String>()
        for (b in initialBooks!!) {
            set.addAll(b.authors!!)
        }

        val liveData = bookDao!!.getAllAuthorsLive()
        liveData.observeForever(object : Observer<List<String>> {
            override fun onChanged(strings: List<String>?) {
                assertNotNull(strings)
                assertTrue(set.size == strings!!.size)

                for (aSet in set) {
                    assertTrue(strings.contains(aSet))
                }
                liveData.removeObserver(this)
            }
        })
    }

    @Test
    fun renameAuthorTest() {
        val oldName = "A"
        val newName = "Z"
        bookDao!!.renameAuthor(oldName, newName)

        val oldList = bookDao!!.getByAuthor(oldName)
        assertTrue(oldList.size == 0)

        val newList = bookDao!!.getByAuthor(newName)

        for (b in initialBooks!!) {
            if (b.authors!!.contains(oldName) || b.authors!!.contains(newName)) {
                var found = false
                for (n in newList) {
                    if (n.bookId == b.bookId) {
                        found = true
                        break
                    }
                }
                assertTrue(found)
            }
        }
    }

    @Test
    fun deleteOtherThanTheseIdsTest() {
        val count = initialBooks!!.size / 2
        val ids = ArrayList<String>()
        for (i in 0 until count) {
            ids.add(initialBooks!![i].bookId)
        }

        bookDao!!.deleteOtherThanTheseIds(*ids.toTypedArray())

        val books = bookDao!!.all

        for (i in initialBooks!!.indices) {
            if (i < count) {
                assertTrue(books.contains(initialBooks!![i]))
            } else {
                assertFalse(books.contains(initialBooks!![i]))
            }
        }
    }

    @Test
    fun deleteAllTest() {
        bookDao!!.deleteAll()
        val books = bookDao!!.all
        assertTrue(books.size == 0)
    }

    companion object {

        private var bookDao: BookDao? = null
        private var initialBooks: MutableList<Book>? = null

        @BeforeClass
        fun set() {
            val context = InstrumentationRegistry.getTargetContext()
            val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            bookDao = database.bookDao()

            initialBooks = ArrayList()
            initialBooks!!.add(newBook("1", "Title1", "SortTitle1", "A,B", "Title1", "A,D"))
            initialBooks!!.add(newBook("4", "Title4", "SortTitle4", "A", "Title4", "A"))
            initialBooks!!.add(newBook("3", "Title3", "SortTitle3", "C", "Title3", "C"))
            initialBooks!!.add(newBook("2", "Title2", "SortTitle2", "B", "Title2", "D"))
        }

        fun newBook(id: String, title: String, sortTitle: String,
                    authors: String, originalTitle: String, originalAuthors: String): Book {
            val book = Book(id)
            book.title = title
            book.sortTitle = sortTitle
            book.originalTitle = originalTitle
            val authorsList = Arrays.asList(*authors.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            book.authors = authorsList
            val originalAuthorsList = Arrays.asList(*originalAuthors.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            book.originalAuthors = originalAuthorsList
            return book
        }
    }

}
