package com.fthdgn.books.view

import android.content.Context
import android.content.Intent
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.fthdgn.books.BaseApplication
import com.fthdgn.books.R
import com.fthdgn.books.activity.MainActivity
import com.fthdgn.books.repository.BookDao
import com.fthdgn.books.utils.bind
import com.fthdgn.books.utils.focusAndShowKeyboard
import com.fthdgn.books.utils.runBackground

class AuthorView : CardView, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private val bookDao: BookDao = BaseApplication.getAppComponent().bookDao()

    private val nameTextView: TextView by bind(R.id.name_text_view)
    private val optionButton: ImageButton by bind(R.id.option_button)
    private val optionPopupMenu: PopupMenu by lazy { PopupMenu(context, optionButton) }
    private lateinit var editMenuItem: MenuItem

    internal var name: CharSequence
        get() = nameTextView.text
        set(name) {
            nameTextView.text = name
        }

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
        View.inflate(context, R.layout.view_authorview, this)
        useCompatPadding = true
        setOnClickListener(this)
        optionButton.setOnClickListener(this)
        optionPopupMenu.setOnMenuItemClickListener(this)
        editMenuItem = optionPopupMenu.menu.add(R.string.edit)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AuthorView)
            name = a.getText(R.styleable.AuthorView_name)
            a.recycle()
        }

        if (isInEditMode && attrs != null) {
            name = attrs.getAttributeValue(TOOLS_NS, "name")
        }
    }

    override fun onClick(v: View) {
        if (v === this) {
            val intent = Intent(v.getContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("AUTHOR", nameTextView.text)
            v.getContext().startActivity(intent)
        }
        if (v === optionButton) {
            optionPopupMenu.show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item === editMenuItem) {
            showEditDialog()
            return true
        }
        return false
    }

    private fun showEditDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.edit_author_name)

        val input = EditText(context)
        input.setSingleLine()
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.setText(name)
        input.layoutParams = lp
        builder.setView(input)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            runBackground {
                bookDao.renameAuthor(nameTextView.text.toString(), input.text.toString())
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
        builder.show()
        input.focusAndShowKeyboard()
    }

    companion object {

        private val TOOLS_NS = "http://schemas.android.com/tools"
    }

}
