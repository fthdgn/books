@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep

@Keep
class EpubPdf {

    var isAvailable: Boolean? = null
    var downloadLink: String? = null
    var acsTokenLink: String? = null

}
