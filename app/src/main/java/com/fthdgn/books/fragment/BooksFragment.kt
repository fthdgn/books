package com.fthdgn.books.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.R
import com.fthdgn.books.repository.Book
import com.fthdgn.books.repository.BookDao
import com.fthdgn.books.view.BookView

class BooksFragment : BaseFragment() {

    private var filterTerm: String? = null

    private var recyclerView: RecyclerView? = null
    private var booksAdapter: BooksAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        @Filter
        val filterType: Int
        if (arguments != null) {
            filterTerm = arguments.getString("filterTerm", "")

            filterType = arguments.getInt("filterType", ALL)
        } else {
            filterType = ALL
        }
        myViewModel.setData(filterType, filterTerm)

        recyclerView = RecyclerView(activity)
        recyclerView!!.id = R.id.recycler_view
        recyclerView!!.layoutManager = GridLayoutManager(activity, activity.resources.getInteger(R.integer.column))
        booksAdapter = BooksAdapter()
        recyclerView!!.adapter = booksAdapter

        myViewModel.getBooks().observe(this, android.arch.lifecycle.Observer { books ->
            booksAdapter!!.setBooks(books)
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            recyclerView

    private inner class BookViewHolder internal constructor(internal val bookView: BookView) : RecyclerView.ViewHolder(bookView)

    private inner class BooksAdapter : RecyclerView.Adapter<BookViewHolder>() {

        private var books: List<Book>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            val bookView = BookView(parent.context)
            return BookViewHolder(bookView)
        }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            holder.bookView.setBook(books?.get(position) ?: Book(""))
        }

        override fun getItemCount(): Int =
                books?.size ?: 0

        internal fun setBooks(books: List<Book>?) {
            this.books = books
            notifyDataSetChanged()
        }
    }

    class MyViewModel : ViewModel() {

        internal val bookDao: BookDao = BaseApplication.getAppComponent().bookDao()

        private var filterTerm: String = ""
        private var filterType: Int = 0

        private val bookLiveData: LiveData<List<Book>> by lazy {
            when (filterType) {
                ALL -> bookDao.allSortedLive
                AUTHORS -> bookDao.getByAuthorLive(filterTerm)
                BOOKSHELF -> bookDao.allSortedLive    //TODO Implement
                else -> bookDao.allSortedLive
            }
        }

        fun setData(filterType: Int, filterTerm: String?) {
            this.filterType = filterType
            this.filterTerm = filterTerm ?: ""
        }

        internal fun getBooks(): LiveData<List<Book>> = bookLiveData
    }

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(ALL.toLong(), AUTHORS.toLong(), BOOKSHELF.toLong())
        internal annotation class Filter


        const private val ALL = 1
        const private val AUTHORS = 2
        const private val BOOKSHELF = 3

        fun allBooksInstance(): BooksFragment = newInstance(ALL, null)

        fun authorBooksInstance(authorName: String): BooksFragment = newInstance(AUTHORS, authorName)

        private fun newInstance(@Filter filterType: Int, filterTerm: String?): BooksFragment {
            val args = Bundle()
            args.putInt("filterType", filterType)
            args.putString("filterTerm", filterTerm)
            val fragment = BooksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
