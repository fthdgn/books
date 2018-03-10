package com.fthdgn.books.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.R
import com.fthdgn.books.repository.BookDao
import com.fthdgn.books.repository.getAllAuthorsLive
import com.fthdgn.books.view.AuthorView

class AuthorsFragment : BaseFragment() {

    private lateinit var recyclerView: RecyclerView
    private val authorsAdapter: AuthorsAdapter = AuthorsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        recyclerView = RecyclerView(activity)
        recyclerView.id = R.id.recycler_view
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = authorsAdapter

        myViewModel.authors.observe(this, Observer { authors ->
            authorsAdapter.setAuthors(authors)
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            recyclerView

    private inner class AuthorsViewHolder internal constructor(internal val authorView: AuthorView) : RecyclerView.ViewHolder(authorView)

    private inner class AuthorsAdapter : RecyclerView.Adapter<AuthorsViewHolder>() {

        private var authors: List<String>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorsViewHolder {
            val authorView = AuthorView(parent.context)
            authorView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            return AuthorsViewHolder(authorView)
        }

        override fun onBindViewHolder(holder: AuthorsViewHolder, position: Int) {
            holder.authorView.name = authors?.get(position) ?: ""
        }

        override fun getItemCount(): Int = authors?.size ?: 0

        internal fun setAuthors(authors: List<String>?) {
            this.authors = authors
            notifyDataSetChanged()
        }
    }

    class MyViewModel : ViewModel() {

        private val bookDao: BookDao = BaseApplication.getAppComponent().bookDao()

        private val authorsLiveData: LiveData<List<String>> by lazy { bookDao.getAllAuthorsLive() }

        internal val authors: LiveData<List<String>>
            get() = authorsLiveData

    }

    companion object {

        fun newInstance(): AuthorsFragment {
            val args = Bundle()
            val fragment = AuthorsFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
