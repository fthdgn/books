@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep
import android.support.annotation.StringDef

@Keep
class IndustryIdentifiers {

    @IndustryIdentifier
    var type: String? = null
    var identifier: String? = null

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(INDUSTRY_IDENTIFIER_ISBN_10, INDUSTRY_IDENTIFIER_ISBN_13, INDUSTRY_IDENTIFIER_ISSN, INDUSTRY_IDENTIFIER_OTHER)
        annotation class IndustryIdentifier

        const val INDUSTRY_IDENTIFIER_ISBN_10 = "ISBN_10"
        const val INDUSTRY_IDENTIFIER_ISBN_13 = "ISBN_13"
        const val INDUSTRY_IDENTIFIER_ISSN = "ISSN"
        const val INDUSTRY_IDENTIFIER_OTHER = "OTHER"
    }
}
