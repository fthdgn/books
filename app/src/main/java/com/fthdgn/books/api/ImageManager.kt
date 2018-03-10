package com.fthdgn.books.api

import android.widget.ImageView

import com.squareup.picasso.Picasso

class ImageManager(private val picasso: Picasso) {

    fun load(id: String, imageView: ImageView) {
        val url = "https://books.google.com/books/content?id=" + id +
                "&printsec=frontcover&img=1&zoom=1&source=gbs_api&w=320"

        picasso.load(url).placeholder(android.R.color.transparent).fit().into(imageView)
    }

}
