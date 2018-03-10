package com.fthdgn.books.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.R
import com.fthdgn.books.api.ImageManager
import com.fthdgn.books.repository.Book
import com.fthdgn.books.repository.BookDao
import com.fthdgn.books.utils.bind
import com.fthdgn.books.utils.focusAndShowKeyboard
import com.fthdgn.books.utils.getViewModel
import com.fthdgn.books.utils.runBackground
import java.util.*

class BookEditActivity : BaseActivity(), View.OnClickListener {

    private val viewModel: MyViewModel by getViewModel(MyViewModel::class.java)
    private val imageManager: ImageManager = BaseApplication.getAppComponent().imageManager()

    private lateinit var bookId: String
    private lateinit var book: Book

    private val coverImageView: ImageView by bind(R.id.cover_image_view)
    private val changeCoverButton: ImageButton  by bind(R.id.change_cover_button)
    private val titleEditText: EditText by bind(R.id.title_edit_text)
    private val sortTitleEditText: EditText  by bind(R.id.sort_title_edit_text)
    private val authorsEditText: EditText   by bind(R.id.authors_edit_text)
    private val titleCheckBox: CheckBox by bind(R.id.title_check_box)
    private val sortTitleCheckBox: CheckBox by bind(R.id.sort_title_check_box)
    private val authorsCheckBox: CheckBox  by bind(R.id.authors_check_box)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_edit)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        titleCheckBox.setOnClickListener(this@BookEditActivity)
        sortTitleCheckBox.setOnClickListener(this@BookEditActivity)
        authorsCheckBox.setOnClickListener(this@BookEditActivity)

        val intent = intent
        bookId = intent.getStringExtra("BOOK_ID")
        viewModel.getBook(bookId).observe(this, android.arch.lifecycle.Observer { book ->
            if (book == null) {
                Toast.makeText(this@BookEditActivity, R.string.activity_start_error, Toast.LENGTH_SHORT).show()
            } else {
                this@BookEditActivity.book = book
                titleCheckBox.isChecked = book.title != book.originalTitle
                titleEditText.isEnabled = titleCheckBox.isChecked
                titleEditText.setText(book.title)

                sortTitleCheckBox.isChecked = book.sortTitle != book.originalTitle
                sortTitleEditText.isEnabled = sortTitleCheckBox.isChecked
                sortTitleEditText.setText(book.sortTitle)

                authorsCheckBox.isChecked = book.formattedAuthors != book.formattedOriginalAuthors
                authorsEditText.isEnabled = authorsCheckBox.isChecked
                authorsEditText.setText(book.formattedAuthors)

                imageManager.load(bookId, coverImageView)

            }
        })

        changeCoverButton.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putBoolean("title_enabled", titleCheckBox.isChecked)
        outState.putBoolean("sort_title_enabled", sortTitleCheckBox.isChecked)
        outState.putBoolean("authors_enabled", authorsCheckBox.isChecked)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        titleCheckBox.isChecked = savedInstanceState.getBoolean("title_enabled", false)
        sortTitleCheckBox.isChecked = savedInstanceState.getBoolean("sort_title_enabled", false)
        authorsCheckBox.isChecked = savedInstanceState.getBoolean("authors_enabled", false)

        titleEditText.isEnabled = titleCheckBox.isChecked
        sortTitleEditText.isEnabled = sortTitleCheckBox.isChecked
        authorsEditText.isEnabled = authorsCheckBox.isChecked
        setNextFocuses()
    }

    override fun onClick(v: View) {
        if (v === titleCheckBox) {
            titleEditText.isEnabled = titleCheckBox.isChecked
            setNextFocuses()
            if (!titleCheckBox.isChecked)
                titleEditText.setText(book.originalTitle)
            else
                titleEditText.focusAndShowKeyboard()
        } else if (v === sortTitleCheckBox) {
            sortTitleEditText.isEnabled = sortTitleCheckBox.isChecked
            setNextFocuses()
            if (!sortTitleCheckBox.isChecked)
                sortTitleEditText.setText(book.originalTitle)
            else
                sortTitleEditText.focusAndShowKeyboard()
        } else if (v === authorsCheckBox) {
            authorsEditText.isEnabled = authorsCheckBox.isChecked
            setNextFocuses()
            if (!authorsCheckBox.isChecked)
                authorsEditText.setText(book.formattedOriginalAuthors)
            else
                authorsEditText.focusAndShowKeyboard()
        }
        if (v === changeCoverButton) {
            //TODO Implement change cover
            Toast.makeText(this, "Click Change Cover Button", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setNextFocuses() {
        val editTexts: ArrayList<EditText> = ArrayList()
        if (titleEditText.isEnabled)
            editTexts.add(titleEditText)
        if (sortTitleEditText.isEnabled)
            editTexts.add(sortTitleEditText)
        if (authorsEditText.isEnabled)
            editTexts.add(authorsEditText)

        for (i in editTexts.indices) {
            val editText = editTexts[i]
            if (i + 1 >= editTexts.size) {
                editText.imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            } else {
                editText.imeOptions = EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_EXTRACT_UI
                editText.nextFocusForwardId = editTexts[i + 1].id
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_book, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.save_button) {
            save()
            return true
        } else if (id == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        book.title = titleEditText.text.toString()
        book.sortTitle = sortTitleEditText.text.toString()
        book.formattedAuthors = authorsEditText.text.toString()

        viewModel.saveBook(book)

        finish()
    }

    internal class MyViewModel : ViewModel() {

        private lateinit var bookId: String
        private val bookLiveData: LiveData<Book> by lazy { bookDao.getByIdLive(bookId) }
        private val bookDao: BookDao = BaseApplication.getAppComponent().bookDao()

        fun getBook(bookId: String): LiveData<Book> {
            this.bookId = bookId
            return bookLiveData
        }

        fun saveBook(book: Book) {
            runBackground({
                bookDao.insertAll(book)
            })
        }
    }


}
