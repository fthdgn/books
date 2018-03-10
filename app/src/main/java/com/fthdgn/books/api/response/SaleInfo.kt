@file:Suppress("unused")

package com.fthdgn.books.api.response

import android.support.annotation.Keep
import android.support.annotation.StringDef
import java.util.*

@Keep
class SaleInfo {

    var country: String? = null
    @Saleability
    var saleability: String? = null
    var onSaleDate: Date? = null
    internal var isEbook: Boolean = false
    var listPrice: Price? = null
    var retailPrice: Price? = null
    var offers: Array<Offer>? = null
    var buyLink: String? = null

    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @StringDef(SALEABILITY_FOR_SALE, SALEABILITY_FREE, SALEABILITY_NOT_FOR_SALE, SALEABILITY_FOR_PREORDER)
        annotation class Saleability

        const val SALEABILITY_FOR_SALE = "FOR_SALE"
        const val SALEABILITY_FREE = "FREE"
        const val SALEABILITY_NOT_FOR_SALE = "NOT_FOR_SALE"
        const val SALEABILITY_FOR_PREORDER = "FOR_PREORDER"
    }

}
