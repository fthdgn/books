package com.fthdgn.books.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.focusAndShowKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    setSelection(text.length)
}