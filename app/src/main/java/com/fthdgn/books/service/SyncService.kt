package com.fthdgn.books.service

import android.app.IntentService
import android.content.Intent
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.api.GoogleBooksApi
import com.fthdgn.books.api.response.Volume
import com.fthdgn.books.repository.Book
import com.fthdgn.books.repository.BookDao
import java.io.IOException
import java.util.*

class SyncService : IntentService("SyncService") {

    private val googleBooksApi: GoogleBooksApi = BaseApplication.getAppComponent().googleBooksApi()
    private val bookDao: BookDao = BaseApplication.getAppComponent().bookDao()

    override fun onHandleIntent(intent: Intent?) {
        val volumesList = ArrayList<Volume>()
        var totalCount: Int
        var lastCount: Int
        var lastIndex = 0
        do {
            try {
                val volumes = googleBooksApi.getMyVolumes(lastIndex).execute().body() ?: return
                //TODO Handle error
                totalCount = volumes.totalItems
                lastCount = if (volumes.items == null) 0 else volumes.items!!.size
                lastIndex += lastCount
                if (volumes.items != null) {
                    volumesList.addAll(volumes.items!!)
                }
            } catch (e: IOException) {
                //TODO Handle error
                e.printStackTrace()
                return
            }

        } while (totalCount > 0 && lastCount > 0)

        if (volumesList.size == totalCount) {
            val ids = volumesList.indices.map { volumesList[it].id }
            val ar = ids.toTypedArray()
            bookDao.deleteOtherThanTheseIds(*ar)
        }

        val books = arrayListOf<Book>()

        for (i in volumesList.indices) {
            val volume = volumesList[i]
            var book: Book? = bookDao.getById(volume.id)
            if (book == null) {
                book = Book(volume.id)
            }
            book.originalAuthors = Arrays.asList(*volume.volumeInfo!!.authors!!)
            book.originalTitle = volume.volumeInfo!!.title
            if (book.authors == null || book.authors!!.isEmpty())
                book.authors = book.originalAuthors

            if (book.title?.isEmpty() != false)
                book.title = book.originalTitle

            if (book.sortTitle?.isEmpty() != false)
                book.sortTitle = book.title

            books.add(book)
        }
        val ar = books.toTypedArray()

        bookDao.insertAll(*ar)
    }
}
