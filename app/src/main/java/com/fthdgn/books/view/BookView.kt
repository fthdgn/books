package com.fthdgn.books.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.view.ViewCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.R
import com.fthdgn.books.activity.BookEditActivity
import com.fthdgn.books.api.ImageManager
import com.fthdgn.books.repository.Book
import com.fthdgn.books.utils.bind

class BookView : CardView, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private val imageManager: ImageManager = BaseApplication.getAppComponent().imageManager()

    private var book: Book? = null

    private val coverImageView: ImageView  by bind(R.id.cover_image_view)
    private val titleTextView: TextView    by bind(R.id.title_text_view)
    private val authorTextView: TextView   by bind(R.id.author_text_view)
    private val optionButton: ImageButton  by bind(R.id.option_button)
    private val optionPopupMenu: PopupMenu by lazy { PopupMenu(context, optionButton) }
    private lateinit var editMenuItem: MenuItem
    private lateinit var aboutMenuItem: MenuItem

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        radius = 5f
        ViewCompat.setElevation(this, 5f)
        View.inflate(context, R.layout.view_bookview, this)
        useCompatPadding = true
        optionButton.setOnClickListener(this)
        optionPopupMenu.setOnMenuItemClickListener(this)
        editMenuItem = optionPopupMenu.menu.add(R.string.edit)
        aboutMenuItem = optionPopupMenu.menu.add(R.string.about)

        setOnClickListener(this)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.BookView)
            setTitle(a.getText(R.styleable.BookView_title))
            coverImageView.setImageResource(a.getResourceId(R.styleable.BookView_cover, 0))
            setAuthor(a.getText(R.styleable.BookView_author))
            a.recycle()
        }

        if (isInEditMode && attrs != null) {
            setTitle(attrs.getAttributeValue(TOOLS_NS, "title"))
            setAuthor(attrs.getAttributeValue(TOOLS_NS, "author"))
            coverImageView.setImageResource(attrs.getAttributeResourceValue(TOOLS_NS, "cover", 0))
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(this)
    }

    fun setBook(book: Book) {
        this.book = book
        setTitle(book.title)
        setAuthor(book.formattedAuthors)
        setCover()
    }

    private fun setTitle(charSequence: CharSequence?) {
        titleTextView.text = charSequence
    }

    private fun setCover() {
        imageManager.load(this.book!!.bookId, coverImageView)
    }

    private fun setAuthor(string: CharSequence) {
        authorTextView.text = string
    }

    override fun onClick(v: View) {
        if (v == optionButton) {
            optionPopupMenu.show()
        } else if (v == this) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/books/reader?id=" + book!!.bookId)
            context.startActivity(intent)
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item === editMenuItem) {
            val intent = Intent(context, BookEditActivity::class.java)
            intent.putExtra("BOOK_ID", book!!.bookId)
            context.startActivity(intent)
            return true
        } else if (item === aboutMenuItem) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/books/details?id=" + book!!.bookId)
            context.startActivity(intent)
            return true
        }
        return false
    }

    companion object {

        private val TOOLS_NS = "http://schemas.android.com/tools/bookview"
    }

    //endregion
}
