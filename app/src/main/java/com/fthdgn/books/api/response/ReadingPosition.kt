@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep
import java.util.*

@Keep
class ReadingPosition {

    /**
     * [.KIND_BOOKS_READINGPOSITION]
     */
    var kind: String? = null
    var volumeId: String? = null
    var gbTextPosition: String? = null
    var gbImagePosition: String? = null
    var epubCfiPosition: String? = null
    var pdfPosition: String? = null
    var updated: Date? = null

    companion object {

        val KIND_BOOKS_READINGPOSITION = "books#readingPosition"
    }

}
