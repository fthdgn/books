package com.fthdgn.books.callback

import android.content.Intent

class ActivityResultListener(val a: (Int, Intent?) -> Unit) {
    fun onActivityResult(code: Int, data: Intent?) = a(code, data)
}
