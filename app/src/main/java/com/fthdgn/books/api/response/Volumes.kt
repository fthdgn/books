@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep

@Keep
class Volumes {

    /**
     * [.KIND_BOOKS_VOLUMES]
     */
    var kind: String? = null
    var totalItems: Int = 0
    var items: List<Volume>? = null

    companion object {

        val KIND_BOOKS_VOLUMES = "books#volumes"
    }

}
