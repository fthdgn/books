package com.fthdgn.books.utils


fun runBackground(r: () -> Unit) {
    Thread(Runnable(r)).start()
}
